package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import com.bottlerocketstudios.brarchitecture.data.serialization.ProtectedPropertyAdapter
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import org.junit.Test

/** These tests exist to show how moshi deals with explicit null (uses null for values) vs implicit nulls (keys not present - uses default values) */
class AccessTokenAdapterTest : BaseTest() {

    @Test
    fun accessTokenAdapterFromJson_validInput_returnsParsedModel() {
        val adapter = Moshi.Builder().add(ProtectedPropertyAdapter()).build().adapter(AccessToken::class.java)
        val expectedResult = AccessToken(accessToken = "at".toProtectedProperty(), scopes = "s", expiresInSeconds = 10, refreshToken = "rt".toProtectedProperty(), tokenType = "tt")

        val result = adapter.fromJson(
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
        val adapter = Moshi.Builder().add(ProtectedPropertyAdapter()).build().adapter(AccessToken::class.java)
        val expectedResult = AccessToken(accessToken = null, scopes = null, expiresInSeconds = 0, refreshToken = null, tokenType = null)

        val result = adapter.fromJson(
            """
            { 
                "access_token": null,
                "token_type": null,
                "refresh_token": null,
                "scopes": null
             }
            """
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun accessTokenAdapterFromJson_invalidInput_returnsModelWithDefaultValues() {
        val adapter = Moshi.Builder().add(ProtectedPropertyAdapter()).build().adapter(AccessToken::class.java)
        val expectedResult = AccessToken(accessToken = "".toProtectedProperty(), scopes = "", expiresInSeconds = 0, refreshToken = "".toProtectedProperty(), tokenType = "")

        val result = adapter.fromJson(
            """
            {}
            """
        )

        assertThat(result).isEqualTo(expectedResult)
    }
}
