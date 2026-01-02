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
            engine {
                addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    if (response.code == 401) {
                        val authHeader = response.header("WWW-Authenticate")
                        if (authHeader != null && authHeader.startsWith("OAuth")) {
                            return@addInterceptor response.newBuilder()
                                .header("WWW-Authenticate", authHeader.replaceFirst("OAuth", "Bearer"))
                                .build()
                        }
                    }
                    response
                }
            }

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
                    LogLevel.ALL
                } else {
                    LogLevel.NONE
                }
            }

            // Bearer token authentication
            install(Auth) {
                bearer {
                    sendWithoutRequest { true }
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
                        Timber.v("Refreshing token: $currentToken")

                        val clientAuthHeader = "Basic " + android.util.Base64.encodeToString(
                            "${com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_KEY}:${com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_SECRET}".toByteArray(),
                            android.util.Base64.NO_WRAP
                        )

                        // Helper to perform password grant
                        suspend fun refreshWithPassword(): BearerTokens? {
                            val response = authClient.submitForm(
                                url = "https://bitbucket.org/site/oauth2/access_token",
                                formParameters = parameters {
                                    append("grant_type", "password")
                                    append("username", credentials?.id?.value.orEmpty())
                                    append("password", credentials?.password?.value.orEmpty())
                                }
                            ) {
                                header(HttpHeaders.Authorization, clientAuthHeader)
                            }

                            return if (response.status == HttpStatusCode.OK) {
                                val newToken = response.body<AccessToken>()
                                credentialsRepo.storeToken(newToken)
                                BearerTokens(
                                    accessToken = newToken.accessToken?.value.orEmpty(),
                                    refreshToken = newToken.refreshToken?.value.orEmpty()
                                )
                            } else {
                                Timber.e("Failed to refresh token with password grant. Status: ${response.status}")
                                null
                            }
                        }

                        // Try refresh token first if available
                        val refreshToken = currentToken?.refreshToken?.value
                        if (!refreshToken.isNullOrEmpty()) {
                            val response = authClient.submitForm(
                                url = "https://bitbucket.org/site/oauth2/access_token",
                                formParameters = parameters {
                                    append("grant_type", "refresh_token")
                                    append("refresh_token", refreshToken)
                                }
                            ) {
                                header(HttpHeaders.Authorization, clientAuthHeader)
                            }

                            if (response.status == HttpStatusCode.OK) {
                                val newToken = response.body<AccessToken>()
                                credentialsRepo.storeToken(newToken)
                                BearerTokens(
                                    accessToken = newToken.accessToken?.value.orEmpty(),
                                    refreshToken = newToken.refreshToken?.value.orEmpty()
                                )
                            } else {
                                Timber.w("Refresh token failed (Status: ${response.status}), falling back to password grant")
                                refreshWithPassword()
                            }
                        } else {
                            // No refresh token, try password grant
                            refreshWithPassword()
                        }
                    }
                }
            }
        }
    }
}
