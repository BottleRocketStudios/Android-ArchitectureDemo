package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.converter.convertToComment
import com.bottlerocketstudios.brarchitecture.data.converter.convertToGitRepository
import com.bottlerocketstudios.brarchitecture.data.converter.convertToSnippet
import com.bottlerocketstudios.brarchitecture.data.converter.convertToWorkspace
import com.bottlerocketstudios.brarchitecture.data.converter.toBranch
import com.bottlerocketstudios.brarchitecture.data.converter.toCommit
import com.bottlerocketstudios.brarchitecture.data.converter.toProject
import com.bottlerocketstudios.brarchitecture.data.converter.toPullRequest
import com.bottlerocketstudios.brarchitecture.data.converter.toRepoFile
import com.bottlerocketstudios.brarchitecture.data.converter.toSnippetDetails
import com.bottlerocketstudios.brarchitecture.data.converter.toUser
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.ParentSnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.ProjectDto
import com.bottlerocketstudios.brarchitecture.data.model.PullRequestDto
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapper
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentContentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketServiceKtor
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthServiceKtor
import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import com.bottlerocketstudios.brarchitecture.domain.models.Commit
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Project
import com.bottlerocketstudios.brarchitecture.domain.models.PullRequest
import com.bottlerocketstudios.brarchitecture.domain.models.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.models.Snippet
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace
import com.bottlerocketstudios.brarchitecture.domain.models.alsoOnSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.asSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.logWrappedExceptions
import com.bottlerocketstudios.brarchitecture.domain.models.map
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@Suppress("TooManyFunctions")
class BitbucketRepositoryImpl : BitbucketRepository, KoinComponent {
        // DI
        private val bitbucketService: BitbucketServiceKtor by inject()
        private val bitbucketCredentialsRepository: BitbucketCredentialsRepository by inject()
        private val responseToApiResultMapper: ResponseToApiResultMapper by inject()
        private val tokenService: TokenAuthServiceKtor by inject()

        // TODO: Move user specific logic to a separate UserRepository
        private val _user = MutableStateFlow<UserDto?>(null)
        private val _repos = MutableStateFlow<List<GitRepositoryDto>>(emptyList())
        private val _snippets = MutableStateFlow<List<SnippetDto>>(emptyList())
        private val _pullRequests = MutableStateFlow<List<PullRequestDto>>(emptyList())
        private val _projects = MutableStateFlow<List<ProjectDto>>(emptyList())
        private val _workspaces = MutableStateFlow<List<Workspace>>(emptyList())

        var authenticated = false
                private set

        override val user: Flow<User?> = _user.map { it?.toUser() }
        override val repos: Flow<List<GitRepository>> =
                _repos.map { list -> list.map { it.convertToGitRepository() } }
        override val snippets: Flow<List<Snippet>> =
                _snippets.map { list -> list.map { it.convertToSnippet() } }
        override val pullRequests: Flow<List<PullRequest>> =
                _pullRequests.map { list -> list.map { it.toPullRequest() } }
        override val projects: Flow<List<Project>> =
                _projects.map { list -> list.map { it.toProject() } }

        override suspend fun authenticate(authCode: String): Boolean {
                Timber.v("[authenticate]")

                return try {
                        val token = tokenService.getAuthCodeToken(authCode)
                        bitbucketCredentialsRepository.storeToken(token)
                        authenticated = true
                        true
                } catch (e: Exception) {
                        Timber.w(e, "[authenticate] failed")
                        authenticated = false
                        false
                }
        }

        override suspend fun authenticate(creds: ValidCredentialModel?): Boolean {
                Timber.v("[authenticate]")
                if (authenticated) {
                        return true
                }
                creds?.let { bitbucketCredentialsRepository.storeCredentials(it) }
                return when (refreshUser()) {
                        is Status.Success -> {
                                authenticated = true
                                true
                        }
                        is Status.Failure -> false
                }
        }

        override suspend fun refreshUser(): Status<Unit> =
                wrapRepoExceptions("refreshUser") {
                        val user = bitbucketService.getUser()
                        _user.value = user
                        // Also fetch workspaces to have them available for default repo lookups
                        getWorkspaces().alsoOnSuccess { _workspaces.value = it }
                        Unit.asSuccess()
                }

        override suspend fun refreshMyRepos(): Status<Unit> =
                wrapRepoExceptions("refreshMyRepos") {
                        val workspaceSlug =
                                _workspaces.value.firstOrNull()?.slug ?: _user.value?.username ?: ""
                        Timber.d("refreshMyRepos: workspaceSlug=$workspaceSlug")
                        val pagedResponse = bitbucketService.getRepositories(workspaceSlug)
                        _repos.value = pagedResponse.values.orEmpty()
                        Timber.d("refreshMyRepos: fetched ${_repos.value.size} repositories")
                        Unit.asSuccess()
                }

        override suspend fun refreshMySnippets(): Status<Unit> =
                wrapRepoExceptions("refreshMySnippets") {
                        val pagedResponse = bitbucketService.getSnippets()
                        val snippets = pagedResponse.values.orEmpty()
                        _snippets.value = snippets
                        Unit.asSuccess()
                }

        override suspend fun getRepositories(workspaceSlug: String): Status<List<GitRepository>> =
                wrapRepoExceptions("getRepositories") {
                        bitbucketService
                                .getRepositories(workspaceSlug)
                                .values
                                .orEmpty()
                                .map { it.convertToGitRepository() }
                                .asSuccess()
                }

        override suspend fun getRepository(
                workspaceSlug: String,
                repo: String
        ): Status<GitRepository> =
                wrapRepoExceptions("getRepository") {
                        bitbucketService
                                .getRepository(workspaceSlug, repo)
                                .convertToGitRepository()
                                .asSuccess()
                }

        override suspend fun getSource(
                workspaceSlug: String,
                repo: String
        ): Status<List<RepoFile>> =
                wrapRepoExceptions("getSource") {
                        bitbucketService
                                .getRepositorySource(workspaceSlug, repo)
                                .values
                                .orEmpty()
                                .map { it.toRepoFile() }
                                .asSuccess()
                }

        override suspend fun getSourceFolder(
                workspaceSlug: String,
                repo: String,
                hash: String,
                path: String
        ): Status<List<RepoFile>> =
                wrapRepoExceptions("getSourceFolder") {
                        bitbucketService
                                .getRepositorySourceFolder(workspaceSlug, repo, hash, path)
                                .values
                                .orEmpty()
                                .map { it.toRepoFile() }
                                .asSuccess()
                }

        override suspend fun getCommits(
                workspaceSlug: String,
                repo: String,
                branch: String
        ): Status<List<Commit>> =
                wrapRepoExceptions("getCommits") {
                        bitbucketService
                                .getRepositoryCommits(workspaceSlug, repo, branch)
                                .values
                                .orEmpty()
                                .map { it.toCommit() }
                                .asSuccess()
                }

        override suspend fun getBranches(
                workspaceSlug: String,
                repo: String
        ): Status<List<Branch>> =
                wrapRepoExceptions("getBranches") {
                        bitbucketService
                                .getRepositoryBranches(workspaceSlug, repo)
                                .values
                                .orEmpty()
                                .map { it.toBranch() }
                                .asSuccess()
                }

        override suspend fun getSourceFile(
                workspaceSlug: String,
                repo: String,
                hash: String,
                path: String
        ): Status<ByteArray> =
                wrapRepoExceptions("getSourceFile") {
                        val response =
                                bitbucketService.getRepositorySourceFile(
                                        workspaceSlug,
                                        repo,
                                        hash,
                                        path
                                )
                        responseToApiResultMapper.toResult(response, response.readRawBytes())
                }

        override suspend fun getPullRequests(workspaceSlug: String?): Status<List<PullRequest>> =
                wrapRepoExceptions("getPullRequests") {
                        val workspace = _workspaces.value.firstOrNull()?.slug ?: ""
                        val repo =
                                _repos.value.firstOrNull()?.slug
                                        ?: _repos.value.firstOrNull()?.name ?: ""
                        Timber.d("getPullRequests: workspace=$workspace, repo=$repo")
                        val pagedResponse = bitbucketService.getPullRequests(workspace, repo)
                        val pullRequests = pagedResponse.values.orEmpty()
                        Timber.d("getPullRequests: fetched ${pullRequests.size} pull requests")
                        _pullRequests.value = pullRequests
                        pullRequests.map { it.toPullRequest() }.asSuccess()
                }

        override suspend fun getPullRequestsWithQuery(state: String): Status<List<PullRequest>> =
                wrapRepoExceptions("getPullRequestsWithQuery") {
                        val workspace = _workspaces.value.firstOrNull()?.slug ?: ""
                        val repo =
                                _repos.value.firstOrNull()?.slug
                                        ?: _repos.value.firstOrNull()?.name ?: ""
                        val pagedResponse =
                                bitbucketService.getPullRequestsWithQuery(workspace, repo, state)
                        val pullRequests = pagedResponse.values.orEmpty()
                        _pullRequests.value = pullRequests
                        pullRequests.map { it.toPullRequest() }.asSuccess()
                }

        override suspend fun createSnippet(
                title: String,
                filename: String,
                contents: String,
                private: Boolean
        ): Status<Unit> =
                wrapRepoExceptions("createSnippet") {
                        bitbucketService.createSnippet(title, filename, contents, private)
                        Unit.asSuccess()
                }

        override suspend fun deleteSnippet(workspaceId: String, encodedId: String): Status<Unit> =
                wrapRepoExceptions("deleteSnippet") {
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.deleteSnippet(workspaceId, encodedId)
                        )
                }

        override suspend fun getSnippetDetails(
                workspaceId: String,
                encodedId: String
        ): Status<SnippetDetails> =
                wrapRepoExceptions("getSnippetDetails") {
                        bitbucketService
                                .getSnippetDetails(workspaceId, encodedId)
                                .toSnippetDetails()
                                .asSuccess()
                }

        override suspend fun getSnippetComments(
                workspaceId: String,
                encodedId: String
        ): Status<List<SnippetComment>> =
                wrapRepoExceptions("getSnippetComments") {
                        bitbucketService
                                .getSnippetComments(workspaceId, encodedId)
                                .values
                                .orEmpty()
                                .map { it.convertToComment() }
                                .asSuccess()
                }

        override suspend fun createSnippetComment(
                workspaceId: String,
                encodedId: String,
                comment: String
        ): Status<Unit> =
                wrapRepoExceptions("createSnippetComment") {
                        val commentDto =
                                SnippetCommentDto(content = SnippetCommentContentDto(raw = comment))
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.createSnippetComment(
                                        workspaceId,
                                        encodedId,
                                        commentDto
                                )
                        )
                }

        override suspend fun createCommentReply(
                workspaceId: String,
                encodedId: String,
                comment: String,
                commentId: Int
        ): Status<Unit> =
                wrapRepoExceptions("createCommentReply") {
                        val commentDto =
                                SnippetCommentDto(
                                        parent = ParentSnippetCommentDto(id = commentId),
                                        content = SnippetCommentContentDto(raw = comment)
                                )
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.createCommentReply(
                                        workspaceId,
                                        encodedId,
                                        commentDto
                                )
                        )
                }

        override suspend fun editSnippetComment(
                workspaceId: String,
                encodedId: String,
                comment: String,
                commentId: Int
        ): Status<Unit> =
                wrapRepoExceptions("editSnippetComment") {
                        val commentDto =
                                SnippetCommentDto(content = SnippetCommentContentDto(raw = comment))
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.editSnippetComment(
                                        workspaceId,
                                        encodedId,
                                        commentId,
                                        commentDto
                                )
                        )
                }

        override suspend fun deleteSnippetComment(
                workspaceId: String,
                encodedId: String,
                commentId: Int
        ): Status<Unit> =
                wrapRepoExceptions("deleteSnippetComment") {
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.deleteSnippetComment(
                                        workspaceId,
                                        encodedId,
                                        commentId
                                )
                        )
                }

        override suspend fun getSnippetFile(
                workspaceId: String,
                encodedId: String,
                filePath: String
        ): Status<ByteArray> =
                wrapRepoExceptions("getSnippetFile") {
                        val response =
                                bitbucketService.getSnippetFile(workspaceId, encodedId, filePath)
                        responseToApiResultMapper.toResult(response, response.readRawBytes())
                }

        override suspend fun isUserWatchingSnippet(
                workspaceId: String,
                encodedId: String
        ): Status<Int> =
                wrapRepoExceptions("isUserWatchingSnippet") {
                        responseToApiResultMapper.toResponseCode(
                                bitbucketService.isUserWatchingSnippet(workspaceId, encodedId)
                        )
                }

        override suspend fun startWatchingSnippet(
                workspaceId: String,
                encodedId: String
        ): Status<Unit> =
                wrapRepoExceptions("startWatchingSnippet") {
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.startWatchingSnippet(workspaceId, encodedId)
                        )
                }

        override suspend fun stopWatchingSnippet(
                workspaceId: String,
                encodedId: String
        ): Status<Unit> =
                wrapRepoExceptions("stopWatchingSnippet") {
                        responseToApiResultMapper.toEmptyResult(
                                bitbucketService.stopWatchingSnippet(workspaceId, encodedId)
                        )
                }

        override suspend fun getProjects(): Status<List<Project>> =
                wrapRepoExceptions("getProjects") {
                        val pagedResponse =
                                bitbucketService.getProjects(_user.value?.username.orEmpty())
                        val projects = pagedResponse.values.orEmpty()
                        _projects.value = projects
                        projects.map { it.toProject() }.asSuccess()
                }

        override suspend fun getWorkspaces(): Status<List<Workspace>> =
                wrapRepoExceptions<List<Workspace>>("getWorkspaces") {
                        bitbucketService
                                .getWorkspaces()
                                .values
                                .orEmpty()
                                .map { it.convertToWorkspace() }
                                .asSuccess<List<Workspace>>()
                }

        override fun clear() {
                bitbucketCredentialsRepository.clearStorage()
                authenticated = false
                _user.value = null
                _repos.value = emptyList()
                _snippets.value = emptyList()
                _workspaces.value = emptyList<Workspace>()
        }

        /**
         * Delegates to [wrapRepoExceptions], passing in the class name here instead of requiring it
         * of all callers
         */
        private suspend fun <T : Any> wrapRepoExceptions(
                methodName: String,
                block: suspend () -> Status<T>
        ): Status<T> {
                return logWrappedExceptions("BitbucketRepositoryImpl.kt", methodName, block)
        }
}
