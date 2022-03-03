package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapper
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.data.model.User
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.model.alsoOnSuccess
import com.bottlerocketstudios.brarchitecture.data.model.asSuccess
import com.bottlerocketstudios.brarchitecture.data.model.map
import com.bottlerocketstudios.brarchitecture.data.model.wrapExceptions
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketService
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import timber.log.Timber

interface BitbucketRepository : com.bottlerocketstudios.brarchitecture.data.repository.Repository {
    val user: StateFlow<User?>
    val repos: StateFlow<List<Repository>>
    val snippets: StateFlow<List<Snippet>>
    suspend fun authenticate(creds: ValidCredentialModel? = null): Boolean
    suspend fun authenticate(authCode: String): Boolean
    suspend fun refreshUser(): ApiResult<Unit>
    suspend fun refreshMyRepos(): ApiResult<Unit>
    suspend fun refreshMySnippets(): ApiResult<Unit>
    suspend fun getRepositories(workspaceSlug: String): ApiResult<List<Repository>>
    suspend fun getRepository(workspaceSlug: String, repo: String): ApiResult<Repository>
    suspend fun getSource(workspaceSlug: String, repo: String): ApiResult<List<RepoFile>>
    suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): ApiResult<List<RepoFile>>
    suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): ApiResult<String>
    suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): ApiResult<Unit>
    fun clear()
}

internal class BitbucketRepositoryImpl(
    private val bitbucketService: BitbucketService,
    private val bitbucketCredentialsRepository: BitbucketCredentialsRepository,
    private val responseToApiResultMapper: ResponseToApiResultMapper
) : BitbucketRepository, KoinComponent {
    // DI
    private val tokenService: TokenAuthService by inject()

    // TODO: Move user specific logic to a separate UserRepository
    private val _user = MutableStateFlow<User?>(null)
    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    private val _snippets = MutableStateFlow<List<Snippet>>(emptyList())
    var authenticated = false
        private set

    override val user: StateFlow<User?> = _user
    override val repos: StateFlow<List<Repository>> = _repos
    override val snippets: StateFlow<List<Snippet>> = _snippets

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
            is ApiResult.Success -> {
                authenticated = true
                true
            }
            is ApiResult.Failure -> false
        }
    }

    override suspend fun refreshUser(): ApiResult<Unit> {
        return wrapExceptions("refreshUser") {
            bitbucketService.getUser().toResult().alsoOnSuccess {
                _user.value = it
            }.map { Unit.asSuccess() }
        }
    }

    override suspend fun refreshMyRepos(): ApiResult<Unit> {
        return wrapExceptions("refreshMyRepos") {
            // TODO: Add support for handling multiple workspaces, as using the user.username might only map to the first workspace created.
            bitbucketService.getRepositories(_user.value?.username ?: "").toResult().alsoOnSuccess {
                _repos.value = it.values.orEmpty()
            }.map { Unit.asSuccess() }
        }
    }

    override suspend fun refreshMySnippets(): ApiResult<Unit> {
        return wrapExceptions("refreshMySnippets") {
            bitbucketService.getSnippets().toResult().map { it.values.orEmpty().asSuccess() }.alsoOnSuccess { snippets: List<Snippet> ->
                _snippets.value = snippets
            }.map { Unit.asSuccess() }
        }
    }

    override suspend fun getRepositories(workspaceSlug: String): ApiResult<List<Repository>> {
        return wrapExceptions("getRepositories") {
            bitbucketService.getRepositories(workspaceSlug).toResult().map { it.values.orEmpty().asSuccess() }
        }
    }

    override suspend fun getRepository(workspaceSlug: String, repo: String): ApiResult<Repository> {
        return wrapExceptions("getRepository") {
            bitbucketService.getRepository(workspaceSlug, repo).toResult()
        }
    }

    override suspend fun getSource(workspaceSlug: String, repo: String): ApiResult<List<RepoFile>> {
        return wrapExceptions("getSource") {
            bitbucketService.getRepositorySource(workspaceSlug, repo).toResult().map { it.values.orEmpty().asSuccess() }
        }
    }

    override suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): ApiResult<List<RepoFile>> {
        return wrapExceptions("getSourceFolder") {
            bitbucketService.getRepositorySourceFolder(workspaceSlug, repo, hash, path).toResult().map { it.values.orEmpty().asSuccess() }
        }
    }

    override suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): ApiResult<String> {
        return wrapExceptions("getSourceFile") {
            bitbucketService.getRepositorySourceFile(workspaceSlug, repo, hash, path).toResult()
        }
    }

    override suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): ApiResult<Unit> {
        return wrapExceptions("createSnippet") {
            val body = MultipartBody.Part.createFormData("file", filename, RequestBody.create(MediaType.get("text/plain"), contents))
            bitbucketService.createSnippet(title, body, private).toEmptyResult()
        }
    }

    override fun clear() {
        bitbucketCredentialsRepository.clearStorage()
        authenticated = false
        _user.value = null
        _repos.value = emptyList()
        _snippets.value = emptyList()
    }

    /** Delegates to [wrapExceptions], passing in the class name here instead of requiring it of all callers */
    private suspend fun <T : Any> wrapExceptions(methodName: String, block: suspend () -> ApiResult<T>): ApiResult<T> {
        return wrapExceptions("BitbucketRepository", methodName, block)
    }

    private fun <T : Any> Response<T>.toResult(): ApiResult<T> {
        return responseToApiResultMapper.toResult(this)
    }

    private fun <T : Any> Response<T>.toEmptyResult(): ApiResult<Unit> {
        return responseToApiResultMapper.toEmptyResult(this)
    }
}
