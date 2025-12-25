package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.component.inject
import timber.log.Timber

/**
 * Ktor HttpClient configured for Bitbucket API with authentication.
 * Replaces BitbucketServiceFactory.
 */
class BitbucketHttpClientFactory : HttpClientFactory() {

    override val baseUrl = "https://api.bitbucket.org/"

    private val credentialsRepo: BitbucketCredentialsRepository by inject()

    /**
     * Create a separate client for token requests (no auth).
     */
    internal val authClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    /**
     * Main API client with bearer token authentication.
     */
    val apiClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS  
                    host = "api.bitbucket.org"
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("Ktor").d(message)
                    }
                }
                level = if (com.bottlerocketstudios.brarchitecture.data.BuildConfig.DEBUG) {
                    LogLevel.HEADERS
                } else {
                    LogLevel.NONE
                }
            }

            // Bearer token authentication
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = credentialsRepo.loadToken()
                        token?.let {
                            BearerTokens(
                                accessToken = it.accessToken?.value.orEmpty(),
                                refreshToken = it.refreshToken?.value.orEmpty()
                            )
                        }
                    }

                    refreshTokens {
                        val currentToken = credentialsRepo.loadToken()
                        val credentials = credentialsRepo.loadCredentials()

                        // If no refresh token, get a new token with credentials
                        if (currentToken?.refreshToken?.value.isNullOrEmpty()) {
                            val response = authClient.submitForm(
                                url = "https://bitbucket.org/site/oauth2/access_token",
                                formParameters = parameters {
                                    append("grant_type", "password")
                                    append("username", credentials?.id?.value.orEmpty())
                                    append("password", credentials?.password?.value.orEmpty())
                                }
                            ) {
                                val authHeader = credentials?.let {
                                    val authString = "${it.id.value}:${it.password.value}"
                                    "Basic ${android.util.Base64.encodeToString(authString.toByteArray(), android.util.Base64.NO_WRAP)}"
                                }
                                authHeader?.let { header(HttpHeaders.Authorization, it) }
                            }

                            val newToken = response.body<AccessToken>()
                            credentialsRepo.storeToken(newToken)

                            BearerTokens(
                                accessToken = newToken.accessToken?.value.orEmpty(),
                                refreshToken = newToken.refreshToken?.value.orEmpty()
                            )
                        } else {
                            // Use refresh token
                            val response = authClient.submitForm(
                                url = "https://bitbucket.org/site/oauth2/access_token",
                                formParameters = parameters {
                                    append("grant_type", "refresh_token")
                                    append("refresh_token", currentToken.refreshToken?.value.orEmpty())
                                }
                            )

                            val newToken = response.body<AccessToken>()
                            credentialsRepo.storeToken(newToken)

                            BearerTokens(
                                accessToken = newToken.accessToken?.value.orEmpty(),
                                refreshToken = newToken.refreshToken?.value.orEmpty()
                            )
                        }
                    }
                }
            }
        }
    }
}
