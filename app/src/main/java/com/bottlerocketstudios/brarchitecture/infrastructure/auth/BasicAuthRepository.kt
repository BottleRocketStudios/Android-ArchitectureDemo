package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import okhttp3.Interceptor


class BasicAuthRepository : AuthRepository {
    override suspend fun authInterceptor(credentials: ValidCredentialModel?): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header("Authorization", AuthRepository.getBasicAuthHeader(credentials?.id?:"", credentials?.password?:""))
                .build()
            chain.proceed(newRequest)
        }
    }
}