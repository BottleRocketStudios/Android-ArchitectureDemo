package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import okhttp3.Interceptor


class BasicAuthRepository : AuthRepository {
    override suspend fun authInterceptor(username: String, password: String): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header("Authorization", AuthRepository.getBasicAuthHeader(username, password))
                .build()
            chain.proceed(newRequest)
        }
    }
}