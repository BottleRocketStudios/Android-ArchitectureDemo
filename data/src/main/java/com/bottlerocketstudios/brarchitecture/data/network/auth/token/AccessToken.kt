package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * See https://developer.atlassian.com/bitbucket/api/2/reference/meta/authentication
 */
@Serializable
internal data class AccessToken(
    @SerialName("access_token") @Contextual val accessToken: ProtectedProperty<String>? = "".toProtectedProperty(),
    @SerialName("scopes") val scopes: String? = "",
    @SerialName("expires_in") val expiresInSeconds: Int? = 0,
    @SerialName("refresh_token") @Contextual val refreshToken: ProtectedProperty<String>? = "".toProtectedProperty(),
    @SerialName("token_type") val tokenType: String? = ""
)
