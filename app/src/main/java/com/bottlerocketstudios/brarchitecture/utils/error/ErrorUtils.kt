package com.bottlerocketstudios.brarchitecture.utils.error

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

private const val ERROR_UNKNOWN = "Unknown Error"

/**
 * Builds an error string from a Throwable object.
 *
 * @return A string containing the error message, code, status, and cause.
 */
suspend fun Throwable.buildExceptionErrorString(): String {
    return when (this) {
        is ResponseException -> {
            val causeException = this.cause as? ResponseException
            val fallbackMessage =
                    if (causeException != null) {
                        "message: ${causeException.message} | code: ${causeException.response.status.value} | status: ${causeException.response.status}"
                    } else {
                        "message: ${this.message}"
                    }

            // project specific error mapping
            return try {
                this.toBitBucketError().message ?: fallbackMessage
            } catch (e: Exception) {
                fallbackMessage
            }
        }
        else -> "message: ${this.message ?: ERROR_UNKNOWN}"
    }
}

private val jsonParser = Json { ignoreUnknownKeys = true }

suspend fun Throwable.toBitBucketError(): BitBucketError {
    return when (this) {
        is ResponseException -> {
            val jsonString = this.response.bodyAsText()
            jsonParser.decodeFromString<BitBucketError>(jsonString)
        }
        else -> BitBucketError()
    }
}
