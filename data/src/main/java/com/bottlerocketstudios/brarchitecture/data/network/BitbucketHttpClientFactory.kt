package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.utils.TAG_KTOR
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.component.inject
import timber.log.Timber

/**
 * Ktor HttpClient configured for Bitbucket API with authentication. Replaces
 * BitbucketServiceFactory.
 */
class BitbucketHttpClientFactory : HttpClientFactory() {

    override val baseUrl = "https://api.bitbucket.org/"

    private val credentialsRepo: BitbucketCredentialsRepository by inject()

    /** Create a separate client for token requests (no auth). */
    internal val authClient: HttpClient by lazy {
        HttpClient(OkHttp) { install(ContentNegotiation) { json(json) } }
    }

    private val ktorLogger =
            object : Logger {
                override fun log(message: String) {
                    Timber.tag(TAG_KTOR).d(message)
                }

                fun d(message: String) = Timber.tag(TAG_KTOR).d(message)
                fun v(message: String) = Timber.tag(TAG_KTOR).v(message)
                fun w(message: String) = Timber.tag(TAG_KTOR).w(message)
                fun e(message: String) = Timber.tag(TAG_KTOR).e(message)
            }

    /** Main API client with bearer token authentication. */
    val apiClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            // Interceptor to rewrite 401 WWW-Authenticate header from OAuth to Bearer since Ktor
            // expects Bearer. Now Ktor refresh token (below) works correctly.
            engine {
                addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    if (response.code == 401) {
                        val authHeader = response.header("WWW-Authenticate")
                        if (authHeader != null && authHeader.startsWith("OAuth")) {
                            return@addInterceptor response.newBuilder()
                                    .header(
                                            "WWW-Authenticate",
                                            authHeader.replaceFirst("OAuth", "Bearer")
                                    )
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

            install(ContentNegotiation) { json(json) }

            install(Logging) {
                logger = ktorLogger
                level =
                        if (com.bottlerocketstudios.brarchitecture.data.BuildConfig.DEBUG) {
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
                        ktorLogger.v("Refreshing token: $currentToken")

                        val clientAuthHeader =
                                "Basic " +
                                        android.util.Base64.encodeToString(
                                                "${com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_KEY}:${com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_SECRET}".toByteArray(),
                                                android.util.Base64.NO_WRAP
                                        )

                        // Helper to perform password grant
                        suspend fun refreshWithPassword(): BearerTokens? {
                            val response =
                                    authClient.submitForm(
                                            url = "https://bitbucket.org/site/oauth2/access_token",
                                            formParameters =
                                                    parameters {
                                                        append("grant_type", "password")
                                                        append(
                                                                "username",
                                                                credentials?.id?.value.orEmpty()
                                                        )
                                                        append(
                                                                "password",
                                                                credentials?.password?.value
                                                                        .orEmpty()
                                                        )
                                                    }
                                    ) { header(HttpHeaders.Authorization, clientAuthHeader) }

                            return if (response.status == HttpStatusCode.OK) {
                                val newToken = response.body<AccessToken>()
                                credentialsRepo.storeToken(newToken)
                                BearerTokens(
                                        accessToken = newToken.accessToken?.value.orEmpty(),
                                        refreshToken = newToken.refreshToken?.value.orEmpty()
                                )
                            } else {
                                ktorLogger.e(
                                        "Failed to refresh token with password grant. Status: ${response.status}"
                                )
                                null
                            }
                        }

                        // Try refresh token first if available
                        val refreshToken = currentToken?.refreshToken?.value
                        if (!refreshToken.isNullOrEmpty()) {
                            val response =
                                    authClient.submitForm(
                                            url = "https://bitbucket.org/site/oauth2/access_token",
                                            formParameters =
                                                    parameters {
                                                        append("grant_type", "refresh_token")
                                                        append("refresh_token", refreshToken)
                                                    }
                                    ) { header(HttpHeaders.Authorization, clientAuthHeader) }

                            if (response.status == HttpStatusCode.OK) {
                                val newToken = response.body<AccessToken>()
                                credentialsRepo.storeToken(newToken)
                                BearerTokens(
                                        accessToken = newToken.accessToken?.value.orEmpty(),
                                        refreshToken = newToken.refreshToken?.value.orEmpty()
                                )
                            } else {
                                ktorLogger.w(
                                        "Refresh token failed (Status: ${response.status}), falling back to password grant"
                                )
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
