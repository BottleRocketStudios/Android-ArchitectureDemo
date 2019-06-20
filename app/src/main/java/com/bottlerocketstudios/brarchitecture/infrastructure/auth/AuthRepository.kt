package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import okhttp3.Interceptor
import org.apache.commons.codec.binary.Base64

interface AuthRepository {
    suspend fun authInterceptor(credentials: ValidCredentialModel): Interceptor

    companion object {
        fun getBasicAuthHeader(username: String, password: String): String {
            val auth = String(Base64.encodeBase64("$username:$password".toByteArray()))
            return "Basic $auth"
        }
    }
}

class AuthenticationFailureException(val error: String) : Throwable()
