package com.bottlerocketstudios.brarchitecture.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.data.model.User
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketService
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BitbucketRepository {
    val user: LiveData<User>
    val repos: LiveData<List<Repository>>
    val snippets: LiveData<List<Snippet>>
    suspend fun authenticate(creds: ValidCredentialModel? = null): Boolean
    fun refreshUser(): Boolean
    fun refreshMyRepos(): Boolean
    fun refreshMySnippets(): Boolean
    fun getRepositories(owner: String): List<Repository>
    fun getRepository(owner: String, repo: String): Repository?
    fun getSource(owner: String, repo: String): List<RepoFile>?
    fun getSourceFolder(owner: String, repo: String, hash: String, path: String): List<RepoFile>?
    fun getSourceFile(owner: String, repo: String, hash: String, path: String): String?
    fun createSnippet(owner: String, title: String, filename: String, contents: String, private: Boolean): Boolean
    fun clear()
}

internal class BitbucketRepositoryImplementation(private val bitbucketService: BitbucketService, private val bitbucketCredentialsRepository: BitbucketCredentialsRepository) : BitbucketRepository {
    private val _user = MutableLiveData<User>()
    private val _repos = MutableLiveData<List<Repository>>()
    private val _snippets = MutableLiveData<List<Snippet>>()
    var authenticated = false
        private set

    override val user: LiveData<User> = _user
    override val repos: LiveData<List<Repository>> = _repos
    override val snippets: LiveData<List<Snippet>> = _snippets

    override suspend fun authenticate(creds: ValidCredentialModel?): Boolean {
        if (authenticated) {
            return true
        }
        creds?.let { bitbucketCredentialsRepository.storeCredentials(it) }
        if (refreshUser()) {
            authenticated = true
            return true
        }
        return false
    }

    override fun refreshUser(): Boolean {
        val response = bitbucketService.getUser().execute()
        var userResponse: User?
        if (response.isSuccessful) {
            userResponse = response.body()
            _user.postValue(userResponse)
        }
        return response.isSuccessful
    }

    override fun refreshMyRepos(): Boolean {
        val response = bitbucketService.getRepositories(_user.value?.username ?: "").execute()
        if (response.isSuccessful) {
            _repos.postValue(response.body()?.values)
        }
        return response.isSuccessful
    }

    override fun refreshMySnippets(): Boolean {
        val response = bitbucketService.getSnippets().execute()
        if (response.isSuccessful) {
            _snippets.postValue(response.body()?.values)
        }
        return response.isSuccessful
    }

    override fun getRepositories(owner: String): List<Repository> {
        val response = bitbucketService.getRepositories(owner).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return emptyList()
    }

    override fun getRepository(owner: String, repo: String): Repository? {
        val response = bitbucketService.getRepository(owner, repo).execute()
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    override fun getSource(owner: String, repo: String): List<RepoFile>? {
        val response = bitbucketService.getRepositorySource(owner, repo).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return null
    }

    override fun getSourceFolder(owner: String, repo: String, hash: String, path: String): List<RepoFile>? {
        val response = bitbucketService.getRepositorySourceFolder(owner, repo, hash, path).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return null
    }

    override fun getSourceFile(owner: String, repo: String, hash: String, path: String): String? {
        val response = bitbucketService.getRepositorySourceFile(owner, repo, hash, path).execute()
        if (response.isSuccessful) {
            return response.body() ?: ""
        }
        return null
    }

    override fun createSnippet(owner: String, title: String, filename: String, contents: String, private: Boolean): Boolean {
        val body = MultipartBody.Part.createFormData("file", filename, RequestBody.create(MediaType.get("text/plain"), contents))
        val response = bitbucketService.createSnippet(title, body, private).execute()
        return response.isSuccessful
    }

    override fun clear() {
        bitbucketCredentialsRepository.clearStorage()
        authenticated = false
        _user.postValue(null)
        _repos.postValue(listOf())
    }
}
