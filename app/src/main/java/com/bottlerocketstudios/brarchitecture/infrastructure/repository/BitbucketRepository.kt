package com.bottlerocketstudios.brarchitecture.infrastructure.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.domain.model.User
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.AuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.AuthenticationFailureException
import com.bottlerocketstudios.brarchitecture.infrastructure.network.BitbucketRetrofit


class BitbucketRepository(val authRepo: AuthRepository) {
    var retrofit = BitbucketRetrofit.getRetrofit(null)
    val user = MutableLiveData<User>()
    var authenticated = false
    
    suspend fun authenticate(creds: ValidCredentialModel) : Boolean {
        if (authenticated) {
            return true
        }
        try {
            val interceptor = authRepo.authInterceptor(creds)
            retrofit = BitbucketRetrofit.getRetrofit(interceptor)
            if (getUser().value != null) {
                authenticated = true
                return true
            }
        } catch (e: AuthenticationFailureException) {
            return false
        }
        return false
    }
    
    suspend fun getUser() : LiveData<User> {
        user.value?.let {
            return user
        }
        val response = retrofit.getUser().execute()
        if (response.isSuccessful) {
            user.postValue(response.body())
        }
        return user
    }
    
    suspend fun getRepositories(owner: String) : List<Repository>  {
        val response = retrofit.getRepositories(owner).execute()
        if (response.isSuccessful) {
            return response.body()?.values?:emptyList()
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
}
