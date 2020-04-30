package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.network.BitbucketFailure
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import timber.log.Timber.e
import java.net.HttpURLConnection

class TokenAuthRepository(val retrofit: Retrofit, val credentialsRepo: BitbucketCredentialsRepository) : AuthRepository {
    var token: AccessToken? = null

    override suspend fun authInterceptor(credentials: ValidCredentialModel?): Interceptor {
        token = credentialsRepo.loadToken()
        var authError: String? = null
        if (token == null) {
            val response =
                retrofit.create(AuthService::class.java).getToken(credentials?.id ?: "", credentials?.password ?: "").execute()
            token = response.body()

            // Uncomment this line, and the authInterceptor will always start out with an expired token
            // token = AccessToken(access_token="mdAoLW3_ug7IPJHSdnn2s_J67sPAnxNbOvVq6ePlOszhqWBxsUUWS4v_ItvhdVnkUxaaxQKn_2jrsXVqDlg=", scopes="project pullrequest", expires_in=7200, refresh_token="WLcfLY3tdXRukHq7kJ", token_type="bearer")
            authError = response.errorBody()?.string()
            authError?.let {
                e(authError)
            }
        }
        token?.let {
            credentialsRepo.storeToken(it)
            it.access_token?.let {
                return Interceptor { chain ->
                    val request = chain.request()
                    val newRequest = request.newBuilder()
                        .header("Authorization", getTokenAuthHeader(it))
                        .build()
                    var chainResult = chain.proceed(newRequest)
                    if (chainResult.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        val failureJson = chainResult.body()?.string() ?: ""
                        val failure =
                            Moshi.Builder().build().adapter(BitbucketFailure::class.java).fromJson(failureJson)
                        if (failure != null && failure.type == "error" && failure.error != null && (failure.error.message
                                ?: "").contains("token expired")
                        ) {
                            val refreshResponse =
                                retrofit.create(AuthService::class.java).refreshToken(token?.refresh_token ?: "")
                                    .execute()
                            token = refreshResponse.body()
                            val newestRequest = request.newBuilder()
                                .header("Authorization", getTokenAuthHeader(token?.access_token ?: ""))
                                .build()
                            chainResult = chain.proceed(newestRequest)
                        }
                    }
                    chainResult
                }
            }
        }
        throw AuthenticationFailureException(authError ?: "Unknown error")
    }

    private fun getTokenAuthHeader(token: String): String {
        return "Bearer " + token
    }

    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    interface AuthService {
        @FormUrlEncoded
        @POST("site/oauth2/access_token")
        fun getToken(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grantType: String? = "password",
            @Header("Authorization") header: String = AuthRepository.getBasicAuthHeader("hqY4kPWYFgYJuCLWhz", "HTnJuCaarHeLTW5hBTJ5pbY5EawZPr65")
        ): Call<AccessToken>

        @FormUrlEncoded
        @POST("site/oauth2/access_token")
        fun refreshToken(
            @Field("refresh_token") username: String,
            @Field("grant_type") grantType: String? = "refresh_token",
            @Header("Authorization") header: String = AuthRepository.getBasicAuthHeader("hqY4kPWYFgYJuCLWhz", "HTnJuCaarHeLTW5hBTJ5pbY5EawZPr65")
        ): Call<AccessToken>
    }

    @JsonClass(generateAdapter = true)
    data class AccessToken(
        var access_token: String? = "",
        var scopes: String? = "",
        var expires_in: Int? = 0,
        var refresh_token: String? = "",
        var token_type: String? = ""
    )
}
