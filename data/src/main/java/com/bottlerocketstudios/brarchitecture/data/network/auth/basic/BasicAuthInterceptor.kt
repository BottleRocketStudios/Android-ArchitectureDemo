package com.bottlerocketstudios.brarchitecture.data.network.auth.basic

import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.getBasicAuthHeader
import okhttp3.Interceptor
import okhttp3.Response

internal class BasicAuthInterceptor(private val credentialsRepo: BitbucketCredentialsRepository) : Interceptor {
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
