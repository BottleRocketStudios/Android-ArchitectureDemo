package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import okhttp3.Interceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


class TokenAuthRepository (val retrofit: Retrofit) : AuthRepository {
    override suspend fun authInterceptor(username: String, password: String): Interceptor {
        val response = retrofit.create(AuthService::class.java).getToken(username, password).execute()
        val token = response.body()?.access_token
        val error = response.errorBody()?.string()
        if (token != null) {
            return Interceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .header("Authorization", getTokenAuthHeader(token))
                    .build()
                chain.proceed(newRequest)
            }
        } else {
            throw AuthenticationFailureException(error?:"Unknown error")
        }

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
    }

    class AccessToken {
        var access_token: String? = ""
        var scopes: String? = ""
        var expires_in: Int? = 0
        var refresh_token: String? = ""
        var token_type: String? = ""
    }
}

