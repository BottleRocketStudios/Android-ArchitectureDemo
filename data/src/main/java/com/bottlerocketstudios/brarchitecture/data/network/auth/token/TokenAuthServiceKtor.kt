package com.bottlerocketstudios.brarchitecture.data.network.auth.token

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Ktor-based service for Bitbucket OAuth token operations.
 * Replaces the Retrofit-based TokenAuthService interface.
 */
internal class TokenAuthServiceKtor : KoinComponent {

    private val client: HttpClient by inject() // Inject the auth-specific client

    /**
     * Get an OAuth access token using username and password.
     */
    suspend fun getToken(
        username: String,
        password: String,
        grantType: String = "password"
    ): AccessToken {
        val authString = "$username:$password"
        val authHeader = "Basic ${android.util.Base64.encodeToString(authString.toByteArray(), android.util.Base64.NO_WRAP)}"

        return client.submitForm(
            url = "https://bitbucket.org/site/oauth2/access_token",
            formParameters = parameters {
                append("grant_type", grantType)
            }
        ) {
            header(HttpHeaders.Authorization, authHeader)
        }.body()
    }

    /**
     * Refresh an OAuth access token using a refresh token.
     */
    suspend fun refreshToken(
        refreshToken: String,
        grantType: String = "refresh_token"
    ): AccessToken {
        return client.submitForm(
            url = "https://bitbucket.org/site/oauth2/access_token",
            formParameters = parameters {
                append("grant_type", grantType)
                append("refresh_token", refreshToken)
            }
        ).body()
    }

    /**
     * Revoke an OAuth access token.
     */
    suspend fun revokeToken(
        token: String,
        username: String,
        password: String
    ): Unit {
        val authString = "$username:$password"
        val authHeader = "Basic ${android.util.Base64.encodeToString(authString.toByteArray(), android.util.Base64.NO_WRAP)}"

        client.post("https://bitbucket.org/site/oauth2/revoke") {
            header(HttpHeaders.Authorization, authHeader)
            setBody(FormDataContent(parameters {
                append("token", token)
            }))
        }
    }
}
