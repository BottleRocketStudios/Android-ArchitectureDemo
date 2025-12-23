package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.data.serialization.ProtectedPropertySerializer
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** These tests exist to show how moshi deals with explicit null (uses null for values) vs implicit nulls (keys not present - uses default values) */
class AccessTokenAdapterTest : BaseTest() {

    private val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

    @Test
    fun accessTokenAdapterFromJson_validInput_returnsParsedModel() {
        val expectedResult = AccessToken(accessToken = "at".toProtectedProperty(), scopes = "s", expiresInSeconds = 10, refreshToken = "rt".toProtectedProperty(), tokenType = "tt")

        val result = json.decodeFromString<AccessToken>(
            """
            { 
                "access_token": "at",
                "token_type": "tt",
                "refresh_token": "rt",
                "expires_in": 10,
                "scopes": "s"
             }
            """
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun accessTokenAdapterFromJson_nullInputValues_returnsModelWithNullsInsteadOfDefaultValues() {
        val expectedResult = AccessToken(accessToken = null, scopes = null, expiresInSeconds = null, refreshToken = null, tokenType = null)

        val result = json.decodeFromString<AccessToken>(
            """
            { 
                "access_token": null,
                "token_type": null,
                "refresh_token": null,
                "scopes": null,
                "expires_in": null
             }
            """
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun accessTokenAdapterFromJson_invalidInput_returnsModelWithDefaultValues() {
        val expectedResult = AccessToken(accessToken = "".toProtectedProperty(), scopes = "", expiresInSeconds = 0, refreshToken = "".toProtectedProperty(), tokenType = "")

        val result = json.decodeFromString<AccessToken>(
            """
            {}
            """
        )

        assertThat(result).isEqualTo(expectedResult)
    }
}
