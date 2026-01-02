package com.bottlerocketstudios.brarchitecture.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/**
 * Factory for creating configured Ktor HttpClient instances.
 * Replaces the previous Retrofit-based ServiceFactory pattern.
 */
abstract class HttpClientFactory : KoinComponent {

    protected val json: Json by inject()

    abstract val baseUrl: String

    protected val client: HttpClient by lazy {
        HttpClient(OkHttp) {
            // Base URL configuration
            defaultRequest {
                url(baseUrl)
            }

            // Content negotiation with Kotlinx Serialization
            install(ContentNegotiation) {
                json(json)
            }

            // Logging
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

            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 30000
            }
        }
    }
}
