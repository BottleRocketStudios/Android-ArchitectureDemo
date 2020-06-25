package com.bottlerocketstudios.brarchitecture.infrastructure.auth.basic

import com.bottlerocketstudios.brarchitecture.infrastructure.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.getBasicAuthHeader
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(private val credentialsRepo: BitbucketCredentialsRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials = credentialsRepo.loadCredentials()
        val request = chain.request()
        val newRequest = request.newBuilder()
            .header(
                "Authorization",
                getBasicAuthHeader(
                    credentials?.id ?: "",
                    credentials?.password ?: ""
                )
            )
            .build()
        return chain.proceed(newRequest)
    }
}
