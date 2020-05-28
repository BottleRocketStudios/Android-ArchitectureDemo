package com.bottlerocketstudios.brarchitecture.infrastructure.auth.token

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessToken(
    val access_token: String? = "",
    val scopes: String? = "",
    val expires_in: Int? = 0,
    val refresh_token: String? = "",
    val token_type: String? = ""
)
