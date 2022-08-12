package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.model.BranchDto
import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.ParentSnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapper
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentContentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketService
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.alsoOnSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.asSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.logWrappedExceptions
import com.bottlerocketstudios.brarchitecture.domain.models.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import timber.log.Timber

interface BitbucketRepository : com.bottlerocketstudios.brarchitecture.domain.models.Repository {
    val user: StateFlow<UserDto?>
    val repos: StateFlow<List<GitRepositoryDto>>
    val snippets: StateFlow<List<SnippetDto>>
    suspend fun authenticate(creds: ValidCredentialModel? = null): Boolean
    suspend fun authenticate(authCode: String): Boolean
    suspend fun refreshUser(): Status<Unit>
    suspend fun refreshMyRepos(): Status<Unit>
    suspend fun refreshMySnippets(): Status<Unit>
    suspend fun getRepositories(workspaceSlug: String): Status<List<GitRepositoryDto>>
    suspend fun getRepository(workspaceSlug: String, repo: String): Status<GitRepositoryDto>
    suspend fun getSource(workspaceSlug: String, repo: String): Status<List<RepoFile>>
    suspend fun getCommits(workspaceSlug: String, repo: String, branch: String): Status<List<CommitDto>>
    suspend fun getBranches(workspaceSlug: String, repo: String): Status<List<BranchDto>>
    suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): Status<List<RepoFile>>
    suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): Status<ByteArray>
    suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): Status<Unit>
    suspend fun deleteSnippet(workspaceId: String, encodedId: String): Status<Unit>
    suspend fun getSnippetDetails(workspaceId: String, encodedId: String): Status<SnippetDetailsDto>
    suspend fun getSnippetComments(workspaceId: String, encodedId: String): Status<List<SnippetCommentDto>>
    suspend fun createSnippetComment(workspaceId: String, encodedId: String, comment: String): Status<Unit>
    suspend fun createCommentReply(workspaceId: String, encodedId: String, comment: String, commentId: Int): Status<Unit>
    suspend fun editSnippetComment(workspaceId: String, encodedId: String, comment: String, commentId: Int): Status<Unit>
    suspend fun deleteSnippetComment(workspaceId: String, encodedId: String, commentId: Int): Status<Unit>
    suspend fun getSnippetFile(workspaceId: String, encodedId: String, filePath: String): Status<ByteArray>
    suspend fun isUserWatchingSnippet(workspaceId: String, encodedId: String): Status<Int>
    suspend fun startWatchingSnippet(workspaceId: String, encodedId: String): Status<Unit>
    suspend fun stopWatchingSnippet(workspaceId: String, encodedId: String): Status<Unit>
    fun clear()
}

@Suppress("TooManyFunctions")
internal class BitbucketRepositoryImpl(
    private val bitbucketService: BitbucketService,
    private val bitbucketCredentialsRepository: BitbucketCredentialsRepository,
    private val responseToApiResultMapper: ResponseToApiResultMapper
) : BitbucketRepository, KoinComponent {
    // DI
    private val tokenService: TokenAuthService by inject()

    // TODO: Move user specific logic to a separate UserRepository
    private val _user = MutableStateFlow<UserDto?>(null)
    private val _repos = MutableStateFlow<List<GitRepositoryDto>>(emptyList())
    private val _snippets = MutableStateFlow<List<SnippetDto>>(emptyList())
    var authenticated = false
        private set

    override val user: StateFlow<UserDto?> = _user
    override val repos: StateFlow<List<GitRepositoryDto>> = _repos
    override val snippets: StateFlow<List<SnippetDto>> = _snippets

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

    override suspend fun getRepositories(workspaceSlug: String): Status<List<GitRepositoryDto>> =
        wrapRepoExceptions("getRepositories") {
            bitbucketService.getRepositories(workspaceSlug)
                .toResult()
                .map { it.values.orEmpty().asSuccess() }
        }

    override suspend fun getRepository(workspaceSlug: String, repo: String): Status<GitRepositoryDto> =
        wrapRepoExceptions("getRepository") {
            bitbucketService.getRepository(workspaceSlug, repo).toResult()
        }

    override suspend fun getSource(workspaceSlug: String, repo: String): Status<List<RepoFile>> =
        wrapRepoExceptions("getSource") {
            bitbucketService.getRepositorySource(workspaceSlug, repo)
                .toResult()
                .map { it.values.orEmpty().asSuccess() }
        }

    override suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): Status<List<RepoFile>> =
        wrapRepoExceptions("getSourceFolder") {
            bitbucketService.getRepositorySourceFolder(workspaceSlug, repo, hash, path)
                .toResult()
                .map { it.values.orEmpty().asSuccess() }
        }

    override suspend fun getCommits(workspaceSlug: String, repo: String, branch: String): Status<List<CommitDto>> {
        return wrapRepoExceptions("getCommits") {
            bitbucketService.getRepositoryCommits(workspaceSlug, repo, branch).toResult().map { it.values.orEmpty().asSuccess() }
        }
    }

    override suspend fun getBranches(workspaceSlug: String, repo: String): Status<List<BranchDto>> {
        return wrapRepoExceptions("getBranches") {
            bitbucketService.getRepositoryBranches(workspaceSlug, repo).toResult().map { it.values.orEmpty().asSuccess() }
        }
    }

    override suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): Status<ByteArray> =
        wrapRepoExceptions("getSourceFile") {
            bitbucketService.getRepositorySourceFile(workspaceSlug, repo, hash, path)
                .toResult()
                .map { it.byteStream().readBytes().asSuccess() }
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

    override suspend fun getSnippetDetails(workspaceId: String, encodedId: String): Status<SnippetDetailsDto> =
        wrapRepoExceptions("getSnippetDetails") {
            bitbucketService.getSnippetDetails(workspaceId, encodedId).toResult()
        }

    override suspend fun getSnippetComments(workspaceId: String, encodedId: String): Status<List<SnippetCommentDto>> =
        wrapRepoExceptions("getSnippetComments") {
            bitbucketService.getSnippetComments(workspaceId, encodedId)
                .toResult()
                .map { it.values.orEmpty().asSuccess() }
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
        return logWrappedExceptions("BitbucketRepository", methodName, block)
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
