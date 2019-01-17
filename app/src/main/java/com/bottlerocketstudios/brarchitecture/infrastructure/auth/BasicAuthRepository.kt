package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import okhttp3.Interceptor
import org.apache.commons.codec.binary.Base64


class BasicAuthRepository : AuthRepository {
    override fun authInterceptor(username: String, password: String): Interceptor {
        System.out.println("Basic "+String(Base64.encodeBase64("${username}:${password}".toByteArray())))
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header("Authorization", getBasicAuthHeader(username, password))
                .build()
            chain.proceed(newRequest)
        }
    }

    private fun getBasicAuthHeader(username: String, password: String): String {
        return "Basic "+Base64.encodeBase64("${username}:${password}".toByteArray())
    }
}