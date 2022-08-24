package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.converter.convertToComment
import com.bottlerocketstudios.brarchitecture.data.converter.convertToGitRepository
import com.bottlerocketstudios.brarchitecture.data.converter.convertToSnippet
import com.bottlerocketstudios.brarchitecture.data.converter.toSnippetDetails
import com.bottlerocketstudios.brarchitecture.data.converter.toUser
import com.bottlerocketstudios.brarchitecture.data.converter.toBranch
import com.bottlerocketstudios.brarchitecture.data.converter.toCommit
import com.bottlerocketstudios.brarchitecture.data.converter.toPullRequest
import com.bottlerocketstudios.brarchitecture.data.converter.toRepoFile
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.ParentSnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.PullRequestDto
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapper
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentContentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketService
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import com.bottlerocketstudios.brarchitecture.domain.models.Commit
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.models.PullRequest
import com.bottlerocketstudios.brarchitecture.domain.models.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.models.Snippet
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.alsoOnSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.asSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.logWrappedExceptions
import com.bottlerocketstudios.brarchitecture.domain.models.map
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import timber.log.Timber


@Suppress("TooManyFunctions")
class BitbucketRepositoryImpl : BitbucketRepository, KoinComponent {
    // DI
    private val bitbucketService: BitbucketService by inject()
    private val bitbucketCredentialsRepository: BitbucketCredentialsRepository by inject()
    private val responseToApiResultMapper: ResponseToApiResultMapper by inject()
    private val tokenService: TokenAuthService by inject()

    // TODO: Move user specific logic to a separate UserRepository
    private val _user = MutableStateFlow<UserDto?>(null)
    private val _repos = MutableStateFlow<List<GitRepositoryDto>>(emptyList())
    private val _snippets = MutableStateFlow<List<SnippetDto>>(emptyList())
    private val _pullRequests = MutableStateFlow<List<PullRequestDto>>(emptyList())

    var authenticated = false
        private set

    override val user: Flow<User?> = _user.map { it?.toUser() }
    override val repos: Flow<List<GitRepository>> = _repos.map { list -> list.map { it.convertToGitRepository() } }
    override val snippets: Flow<List<Snippet>> = _snippets.map { list -> list.map { it.convertToSnippet() } }
    override val pullRequests: Flow<List<PullRequest>> = _pullRequests.map { list -> list.map { it.toPullRequest() } }

    override suspend fun authenticate(authCode: String): Boolean {
        Timber.v("[authenticate]")

        val resp = tokenService.getAuthCodeToken(authCode)
        authenticated = resp.isSuccessful
        resp.body()?.let {
            bitbucketCredentialsRepository.storeToken(it)
        }

        return authenticated
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
            bitbucketService.getUser().toResult()
                .alsoOnSuccess { _user.value = it }
                .map { Unit.asSuccess() }
        }

    override suspend fun refreshMyRepos(): Status<Unit> =
        wrapRepoExceptions("refreshMyRepos") {
            // TODO: Add support for handling multiple workspaces, as using the user.username might only map to the first workspace created.
            bitbucketService.getRepositories(_user.value?.username ?: "").toResult()
                .alsoOnSuccess { _repos.value = it.values.orEmpty() }
                .map { Unit.asSuccess() }
        }

    override suspend fun refreshMySnippets(): Status<Unit> =
        wrapRepoExceptions("refreshMySnippets") {
            bitbucketService.getSnippets().toResult()
                .map { it.values.orEmpty().asSuccess() }
                .alsoOnSuccess { snippets: List<SnippetDto> -> _snippets.value = snippets }
                .map { Unit.asSuccess() }
        }

    override suspend fun getRepositories(workspaceSlug: String): Status<List<GitRepository>> =
        wrapRepoExceptions("getRepositories") {
            bitbucketService.getRepositories(workspaceSlug)
                .toResult()
                .map { response ->
                    response.values.orEmpty().map { list ->
                        list.convertToGitRepository()
                    }.asSuccess()
                }
        }

    override suspend fun getRepository(workspaceSlug: String, repo: String): Status<GitRepository> =
        wrapRepoExceptions("getRepository") {
            bitbucketService.getRepository(workspaceSlug, repo).toResult().map { it.convertToGitRepository().asSuccess() }
        }

    override suspend fun getSource(workspaceSlug: String, repo: String): Status<List<RepoFile>> =
        wrapRepoExceptions("getSource") {
            bitbucketService.getRepositorySource(workspaceSlug, repo).toResult().map { list ->
                list.values.orEmpty().map {
                    it.toRepoFile()
                }.asSuccess()
            }
        }

    override suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): Status<List<RepoFile>> =
        wrapRepoExceptions("getSourceFolder") {
            bitbucketService.getRepositorySourceFolder(workspaceSlug, repo, hash, path).toResult().map { pagedResponse ->
                pagedResponse.values.orEmpty().map {
                    it.toRepoFile()
                }.asSuccess()
            }
        }

    override suspend fun getCommits(workspaceSlug: String, repo: String, branch: String): Status<List<Commit>> = wrapRepoExceptions("getCommits") {
        bitbucketService.getRepositoryCommits(workspaceSlug, repo, branch).toResult().map { pagedResponse ->
            pagedResponse.values.orEmpty().map {
                it.toCommit()
            }.asSuccess()
        }
    }

    override suspend fun getBranches(workspaceSlug: String, repo: String): Status<List<Branch>> = wrapRepoExceptions("getBranches") {
        bitbucketService.getRepositoryBranches(workspaceSlug, repo).toResult().map { pagedResponse ->
            pagedResponse.values.orEmpty().map {
                it.toBranch()
            }.asSuccess()
        }
    }

    override suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): Status<ByteArray> =
        wrapRepoExceptions("getSourceFile") {
            bitbucketService.getRepositorySourceFile(workspaceSlug, repo, hash, path)
                .toResult()
                .map { it.byteStream().readBytes().asSuccess() }
        }

    override suspend fun getPullRequests(): Status<List<PullRequest>> =
        wrapRepoExceptions("getPullRequests") {
            bitbucketService.getPullRequests(_user.value?.username.orEmpty()).toResult()
                .map { pagedResponse ->
                    pagedResponse.values.orEmpty().also {
                        _pullRequests.value = it
                    }.map {
                        it.toPullRequest()
                    }.asSuccess()
                }
        }

    override suspend fun getPullRequestsWithQuery(state: String): Status<List<PullRequest>> =
        wrapRepoExceptions("getPullRequestsWithQuery") {
            bitbucketService.getPullRequestsWithQuery(_user.value?.username.orEmpty(), state).toResult()
                .map { pagedResponse ->
                    pagedResponse.values.orEmpty().also {
                        _pullRequests.value = it
                    }.map {
                        it.toPullRequest()
                    }.asSuccess()
                }
        }

    override suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): Status<Unit> =
        wrapRepoExceptions("createSnippet") {
            val body = MultipartBody.Part.createFormData("file", filename, RequestBody.create(MediaType.get("text/plain"), contents))
            bitbucketService.createSnippet(title, body, private).toEmptyResult()
        }

    override suspend fun deleteSnippet(workspaceId: String, encodedId: String): Status<Unit> =
        wrapRepoExceptions("deleteSnippet") {
            bitbucketService.deleteSnippet(workspaceId, encodedId).toEmptyResult()
        }

    override suspend fun getSnippetDetails(workspaceId: String, encodedId: String): Status<SnippetDetails> =
        wrapRepoExceptions("getSnippetDetails") {
            bitbucketService.getSnippetDetails(workspaceId, encodedId).toResult().map { it.toSnippetDetails().asSuccess() }
        }

    override suspend fun getSnippetComments(workspaceId: String, encodedId: String): Status<List<SnippetComment>> =
        wrapRepoExceptions("getSnippetComments") {
            bitbucketService.getSnippetComments(workspaceId, encodedId)
                .toResult()
                .map { response ->
                    response.values.orEmpty().map {
                        it.convertToComment()
                    }.asSuccess()
                }
        }

    override suspend fun createSnippetComment(workspaceId: String, encodedId: String, comment: String): Status<Unit> =
        wrapRepoExceptions("createSnippetComment") {
            val commentDto = SnippetCommentDto(content = SnippetCommentContentDto(raw = comment))
            bitbucketService.createSnippetComment(workspaceId, encodedId, commentDto).toEmptyResult()
        }

    override suspend fun createCommentReply(workspaceId: String, encodedId: String, comment: String, commentId: Int): Status<Unit> =
        wrapRepoExceptions("createCommentReply") {
            val commentDto = SnippetCommentDto(parent = ParentSnippetCommentDto(id = commentId), content = SnippetCommentContentDto(raw = comment))
            bitbucketService.createCommentReply(workspaceId, encodedId, commentDto).toEmptyResult()
        }

    override suspend fun editSnippetComment(workspaceId: String, encodedId: String, comment: String, commentId: Int): Status<Unit> =
        wrapRepoExceptions("editSnippetComment") {
            val commentDto = SnippetCommentDto(content = SnippetCommentContentDto(raw = comment))
            bitbucketService.editSnippetComment(workspaceId, encodedId, commentId, commentDto).toEmptyResult()
        }

    override suspend fun deleteSnippetComment(workspaceId: String, encodedId: String, commentId: Int): Status<Unit> =
        wrapRepoExceptions("deleteSnippetComment") {
            bitbucketService.deleteSnippetComment(workspaceId, encodedId, commentId).toEmptyResult()
        }

    override suspend fun getSnippetFile(workspaceId: String, encodedId: String, filePath: String): Status<ByteArray> =
        wrapRepoExceptions("getSnippetFile") {
            bitbucketService.getSnippetFile(workspaceId, encodedId, filePath)
                .toResult()
                .map { it.byteStream().readBytes().asSuccess() }
        }

    override suspend fun isUserWatchingSnippet(workspaceId: String, encodedId: String): Status<Int> =
        wrapRepoExceptions("isUserWatchingSnippet") {
            bitbucketService.isUserWatchingSnippet(workspaceId, encodedId).toResponseCode()
        }

    override suspend fun startWatchingSnippet(workspaceId: String, encodedId: String): Status<Unit> =
        wrapRepoExceptions("startWatchingSnippet") {
            bitbucketService.startWatchingSnippet(workspaceId, encodedId).toEmptyResult()
        }

    override suspend fun stopWatchingSnippet(workspaceId: String, encodedId: String): Status<Unit> =
        wrapRepoExceptions("stopWatchingSnippet") {
            bitbucketService.stopWatchingSnippet(workspaceId, encodedId).toEmptyResult()
        }

    override fun clear() {
        bitbucketCredentialsRepository.clearStorage()
        authenticated = false
        _user.value = null
        _repos.value = emptyList()
        _snippets.value = emptyList()
    }

    /** Delegates to [wrapRepoExceptions], passing in the class name here instead of requiring it of all callers */
    private suspend fun <T : Any> wrapRepoExceptions(methodName: String, block: suspend () -> Status<T>): Status<T> {
        return logWrappedExceptions("BitbucketRepositoryImpl.kt", methodName, block)
    }

    private fun <T : Any> Response<T>.toResult(): Status<T> {
        return responseToApiResultMapper.toResult(this)
    }

    private fun <T : Any> Response<T>.toEmptyResult(): Status<Unit> {
        return responseToApiResultMapper.toEmptyResult(this)
    }

    private fun <T : Any> Response<T>.toResponseCode(): Status<Int> {
        return responseToApiResultMapper.toResponseCode(this)
    }
}
