package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.data.BuildConfig
import com.bottlerocketstudios.brarchitecture.data.network.auth.getBasicAuthHeader
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import retrofit2.Call
import retrofit2.Response
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
            BuildConfig.BITBUCKET_KEY.toProtectedProperty(),
            BuildConfig.BITBUCKET_SECRET.toProtectedProperty(),
        )
    ): Call<AccessToken>

    @FormUrlEncoded
    @POST("site/oauth2/access_token")
    fun refreshToken(
        @Field("refresh_token") username: String,
        @Field("grant_type") grantType: String? = "refresh_token",
        @Header("Authorization") header: String = getBasicAuthHeader(
            BuildConfig.BITBUCKET_KEY.toProtectedProperty(),
            BuildConfig.BITBUCKET_SECRET.toProtectedProperty(),
        )
    ): Call<AccessToken>

    @FormUrlEncoded
    @POST("site/oauth2/access_token")
    suspend fun getAuthCodeToken(
        @Field("code") code: String,
        @Field("grant_type") grantType: String? = "authorization_code",
        @Header("Authorization") header: String = getBasicAuthHeader(
            BuildConfig.BITBUCKET_KEY.toProtectedProperty(),
            BuildConfig.BITBUCKET_SECRET.toProtectedProperty(),
        )
    ): Response<AccessToken>
}
