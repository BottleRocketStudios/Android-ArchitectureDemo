package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.data.network.auth.getBasicAuthHeader
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/** Bitbucket auth token related apis. See https://developer.atlassian.com/bitbucket/api/2/reference/meta/authentication */
internal interface TokenAuthService {
    @FormUrlEncoded
    @POST("site/oauth2/access_token")
    fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String? = "password",
        @Header("Authorization") header: String = getBasicAuthHeader(
            "hqY4kPWYFgYJuCLWhz",
            "HTnJuCaarHeLTW5hBTJ5pbY5EawZPr65"
        )
    ): Call<AccessToken>

    @FormUrlEncoded
    @POST("site/oauth2/access_token")
    fun refreshToken(
        @Field("refresh_token") username: String,
        @Field("grant_type") grantType: String? = "refresh_token",
        @Header("Authorization") header: String = getBasicAuthHeader(
            "hqY4kPWYFgYJuCLWhz",
            "HTnJuCaarHeLTW5hBTJ5pbY5EawZPr65"
        )
    ): Call<AccessToken>
}
