package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * See https://developer.atlassian.com/bitbucket/api/2/reference/meta/authentication
 */
@JsonClass(generateAdapter = true)
internal data class AccessToken(
    @Json(name = "access_token") val accessToken: String? = "",
    @Json(name = "scopes") val scopes: String? = "",
    @Json(name = "expires_in") val expiresInSeconds: Int? = 0,
    @Json(name = "refresh_token") val refreshToken: String? = "",
    @Json(name = "token_type") val tokenType: String? = ""
)
