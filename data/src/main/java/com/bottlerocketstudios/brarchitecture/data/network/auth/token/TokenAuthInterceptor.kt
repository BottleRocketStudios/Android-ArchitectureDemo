package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.data.network.BitbucketFailure
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection

internal class TokenAuthInterceptor(private val tokenAuthService: TokenAuthService, private val credentialsRepo: BitbucketCredentialsRepository) : Interceptor {

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
            val call = tokenAuthService.getToken(credentials?.id ?: "", credentials?.password ?: "")
            val response = call.execute()
            val newToken = response.body()
            // Uncomment this line, and the authInterceptor will always start out with an expired token
            // token = AccessToken(access_token="mdAoLW3_ug7IPJHSdnn2s_J67sPAnxNbOvVq6ePlOszhqWBxsUUWS4v_ItvhdVnkUxaaxQKn_2jrsXVqDlg=", scopes="project pullrequest", expires_in=7200, refresh_token="WLcfLY3tdXRukHq7kJ", token_type="bearer")

            if (newToken == null) {
                response.errorBody()?.string()?.also { Timber.w("getToken error=$it") }
            } else {
                credentialsRepo.storeToken(newToken)
            }
        }
    }

    private fun interceptWorker(chain: Interceptor.Chain): Response {
        val token = credentialsRepo.loadToken()
        val accessToken = token?.accessToken
        val refreshToken = token?.refreshToken
        return if (accessToken.isNullOrEmpty()) {
            chain.proceed(chain.request())
        } else {
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header("Authorization", getTokenAuthHeader(accessToken))
                .build()
            var chainResult = chain.proceed(newRequest)
            if (chainResult.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val failureJson = chainResult.body()?.string() ?: ""
                val failure =
                    Moshi.Builder().build().adapter(BitbucketFailure::class.java).fromJson(failureJson)
                Timber.v("auth failure=$failure")
                if (failure != null && failure.type == "error" && failure.error != null && (failure.error.message
                        ?: "").contains("token expired")
                ) {
                    val refreshResponse =
                        tokenAuthService.refreshToken(refreshToken ?: "")
                            .execute()
                    val newToken = refreshResponse.body()

                    if (newToken == null) {
                        refreshResponse.errorBody()?.string()?.also { Timber.w("refreshToken error=$it") }
                    } else {
                        credentialsRepo.storeToken(newToken)
                    }

                    val newestRequest = request.newBuilder()
                        .header("Authorization", getTokenAuthHeader(newToken?.accessToken ?: ""))
                        .build()
                    chainResult = chain.proceed(newestRequest)
                }
            }
            chainResult
        }
    }
}

private fun getTokenAuthHeader(token: String): String {
    return "Bearer " + token
}
