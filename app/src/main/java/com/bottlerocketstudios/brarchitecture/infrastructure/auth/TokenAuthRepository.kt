package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.infrastructure.network.BitbucketFailure
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


class TokenAuthRepository (val retrofit: Retrofit) : AuthRepository {
    var token: AccessToken? = null

    override suspend fun authInterceptor(username: String, password: String): Interceptor {
        val response = retrofit.create(AuthService::class.java).getToken(username, password).execute()
        token = response.body()
        token = AccessToken(access_token="mdAoLW3_ug7IPJHSdnn2s_J67sPAnxNbOvVq6ePlOszhqWBxsUUWS4v_ItvhdVnkUxaaxQKn_2jrsXVqDlg=", scopes="project pullrequest", expires_in=7200, refresh_token="WLcfLY3tdXRukHq7kJ", token_type="bearer")
        val authError = response.errorBody()?.string()
        token?.let {
            it.access_token?.let {
                return Interceptor { chain ->
                    val request = chain.request()
                    val newRequest = request.newBuilder()
                        .header("Authorization", getTokenAuthHeader(it))
                        .build()
                    var chainResult = chain.proceed(newRequest)
                    if (chainResult.code() == 401) {
                        val failureJson = chainResult.body()?.string()?:""
                        val failure = Moshi.Builder().build().adapter(BitbucketFailure::class.java).fromJson(failureJson)
                        if (failure != null && failure.type == "error" && failure.error != null && (failure.error.message?:"").contains("token expired")) {
                            val refreshResponse = retrofit.create(AuthService::class.java).refreshToken(token?.refresh_token?:"").execute()
                            token = refreshResponse.body()
                            val newestRequest = request.newBuilder()
                                .header("Authorization", getTokenAuthHeader(token?.access_token?:""))
                                .build()
                            chainResult = chain.proceed(newestRequest)
                        }
                    }
                    chainResult
                }
            }
        }
        throw AuthenticationFailureException(authError?:"Unknown error")

    }

    private fun getTokenAuthHeader(token: String): String {
        return "Bearer "+token
    }

    interface AuthService {
        @FormUrlEncoded
        @POST("site/oauth2/access_token")
        fun getToken(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grantType: String? = "password",
            @Header("Authorization") header: String = AuthRepository.getBasicAuthHeader("hqY4kPWYFgYJuCLWhz", "HTnJuCaarHeLTW5hBTJ5pbY5EawZPr65"))
                : Call<AccessToken>
        
        @FormUrlEncoded
        @POST("site/oauth2/access_token")
        fun refreshToken(
            @Field("refresh_token") username: String,
            @Field("grant_type") grantType: String? = "refresh_token",
            @Header("Authorization") header: String = AuthRepository.getBasicAuthHeader("hqY4kPWYFgYJuCLWhz", "HTnJuCaarHeLTW5hBTJ5pbY5EawZPr65"))
                : Call<AccessToken>
    }

    data class AccessToken (
        var access_token: String? = "",
        var scopes: String? = "",
        var expires_in: Int? = 0,
        var refresh_token: String? = "",
        var token_type: String? = ""
    )
}

