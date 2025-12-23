package com.bottlerocketstudios.brarchitecture.data.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
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
                    LogLevel.HEADERS
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
