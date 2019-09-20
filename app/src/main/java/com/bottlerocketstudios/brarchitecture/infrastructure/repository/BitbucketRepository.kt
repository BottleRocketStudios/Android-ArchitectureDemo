package com.bottlerocketstudios.brarchitecture.infrastructure.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.domain.model.User
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.AuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.AuthenticationFailureException
import com.bottlerocketstudios.brarchitecture.infrastructure.network.BitbucketRetrofit

class BitbucketRepository(val authRepo: AuthRepository) {
    var retrofit = BitbucketRetrofit.getRetrofit(null)
    private val _user = MutableLiveData<User>()
    private val _repos = MutableLiveData<List<Repository>>()
    var authenticated = false

    val user: LiveData<User>
        get() = _user

    val repos: LiveData<List<Repository>>
        get() = _repos

    suspend fun authenticate(creds: ValidCredentialModel? = null) : Boolean {
        if (authenticated) {
            return true
        }
        try {
            val interceptor = authRepo.authInterceptor(creds)
            retrofit = BitbucketRetrofit.getRetrofit(interceptor)
            if (refreshUser()) {
                authenticated = true
                return true
            }
        } catch (e: AuthenticationFailureException) {
            return false
        }
        return false
    }

    fun refreshUser(): Boolean {
        val response = retrofit.getUser().execute()
        var userResponse: User?
        if (response.isSuccessful) {
            userResponse = response.body()
            _user.postValue(userResponse)
        }
        return response.isSuccessful
    }

    fun refreshMyRepos(): Boolean {
        val response = retrofit.getRepositories(_user.value?.username ?: "").execute()
        if (response.isSuccessful) {
            _repos.postValue(response.body()?.values)
        }
        return response.isSuccessful
    }

    fun getRepositories(owner: String): List<Repository> {
        val response = retrofit.getRepositories(owner).execute()
        if (response.isSuccessful) {
            return response.body()?.values ?: emptyList()
        }
        return emptyList()
    }

    fun getRepository(owner: String, repo: String): Repository? {
        val response = retrofit.getRepository(owner, repo).execute()
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    fun getSource(owner: String, repo: String): List<RepoFile>? {
        val response = retrofit.getRepositorySource(owner, repo).execute()
        if (response.isSuccessful) {
            return response.body()?.values?: emptyList()
        }
        return null
    }

    fun getSourceFolder(owner: String, repo: String, hash: String, path: String): List<RepoFile>? {
        val response = retrofit.getRepositorySourceFolder(owner, repo, hash, path).execute()
        if (response.isSuccessful) {
            return response.body()?.values?: emptyList()
        }
        return null
    }
    
    fun getSourceFile(owner: String, repo: String, hash: String, path: String): String? {
        val response = retrofit.getRepositorySourceFile(owner, repo, hash, path).execute()
        if (response.isSuccessful) {
            return response.body()?:""
        }
        return null
    }
}
