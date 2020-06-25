package com.bottlerocketstudios.brarchitecture.infrastructure.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.User
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.network.BitbucketService

class BitbucketRepository(private val bitbucketService: BitbucketService, private val bitbucketCredentialsRepository: BitbucketCredentialsRepository) {
    private val _user = MutableLiveData<User>()
    private val _repos = MutableLiveData<List<Repository>>()
    var authenticated = false
        private set

    val user: LiveData<User> = _user
    val repos: LiveData<List<Repository>> = _repos

    suspend fun authenticate(creds: ValidCredentialModel? = null): Boolean {
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

    fun refreshUser(): Boolean {
        val response = bitbucketService.getUser().execute()
        var userResponse: User?
        if (response.isSuccessful) {
            userResponse = response.body()
            _user.postValue(userResponse)
        }
        return response.isSuccessful
    }

    fun refreshMyRepos(): Boolean {
        val response = bitbucketService.getRepositories(_user.value?.username ?: "").execute()
        if (response.isSuccessful) {
            _repos.postValue(response.body()?.values)
        }
        return response.isSuccessful
    }

    fun getRepositories(owner: String): List<Repository> {
        val response = bitbucketService.getRepositories(owner).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return emptyList()
    }

    fun getRepository(owner: String, repo: String): Repository? {
        val response = bitbucketService.getRepository(owner, repo).execute()
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    fun getSource(owner: String, repo: String): List<RepoFile>? {
        val response = bitbucketService.getRepositorySource(owner, repo).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return null
    }

    fun getSourceFolder(owner: String, repo: String, hash: String, path: String): List<RepoFile>? {
        val response = bitbucketService.getRepositorySourceFolder(owner, repo, hash, path).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return null
    }

    fun getSourceFile(owner: String, repo: String, hash: String, path: String): String? {
        val response = bitbucketService.getRepositorySourceFile(owner, repo, hash, path).execute()
        if (response.isSuccessful) {
            return response.body() ?: ""
        }
        return null
    }
}
