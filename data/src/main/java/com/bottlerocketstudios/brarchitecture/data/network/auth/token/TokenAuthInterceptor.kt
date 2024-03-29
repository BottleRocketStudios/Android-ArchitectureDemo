package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.data.network.BitbucketFailure
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.net.HttpURLConnection

internal class TokenAuthInterceptor : Interceptor, KoinComponent {

    private val tokenAuthService: TokenAuthService by inject()
    private val credentialsRepo: BitbucketCredentialsRepository by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        getTokenWorker()
        return interceptWorker(chain)
    }

    /**
     * Gets new token if none saved and returns error string.
     */
    private fun getTokenWorker() {
        val token = credentialsRepo.loadToken()
        val credentials = credentialsRepo.loadCredentials()
        if (token == null) {
            val call = tokenAuthService.getToken(credentials?.id?.value.orEmpty(), credentials?.password?.value.orEmpty())
            val response = call.execute()
            val newToken = response.body()

            if (newToken == null) {
                response.errorBody()?.string()?.also { Timber.w("getToken error=$it") }
            } else {
                credentialsRepo.storeToken(newToken)
            }
        }
    }

    @Suppress("NestedBlockDepth") // not sure why detekt flags this as 4 blocks deep when it really is only 1-2 max
    private fun interceptWorker(chain: Interceptor.Chain): Response {
        val token = credentialsRepo.loadToken()
        val accessToken = token?.accessToken
        val refreshToken = token?.refreshToken
        return if (accessToken?.value.isNullOrEmpty()) {
            chain.proceed(chain.request())
        } else {
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header("Authorization", getTokenAuthHeader(accessToken!!.value))
                .build()
            var chainResult = chain.proceed(newRequest)
            if (chainResult.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val failureJson = chainResult.body()?.string() ?: ""
                val failure =
                    Moshi.Builder().build().adapter(BitbucketFailure::class.java).fromJson(failureJson)
                Timber.v("auth failure=$failure")
                if (failure?.type == "error" && failure.error?.message.orEmpty().contains("token expired")) {
                    val refreshResponse =
                        tokenAuthService.refreshToken(refreshToken?.value.orEmpty())
                            .execute()
                    val newToken = refreshResponse.body()

                    if (newToken == null) {
                        refreshResponse.errorBody()?.string()?.also { Timber.w("refreshToken error=$it") }
                    } else {
                        credentialsRepo.storeToken(newToken)
                    }

                    val newestRequest = request.newBuilder()
                        .header("Authorization", getTokenAuthHeader(newToken?.accessToken?.value.orEmpty()))
                        .build()
                    chainResult = chain.proceed(newestRequest)
                }
            }
            chainResult
        }
    }
}

private fun getTokenAuthHeader(token: String): String = "Bearer $token"
