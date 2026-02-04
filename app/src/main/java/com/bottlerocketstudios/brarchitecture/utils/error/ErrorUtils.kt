package com.bottlerocketstudios.brarchitecture.utils.error

import io.ktor.client.plugins.ResponseException

private const val ERROR_UNKNOWN = "Unknown Error"

/**
 * Builds an error string from a Throwable object.
 *
 * @return A string containing the error message, code, status, and cause.
 */
// fun Throwable.buildExceptionErrorString(): String {
//     return when (this) {
//         is ResponseMessageException -> {
//             val causeException = this.cause as? ResponseException
//             val fallbackMessage =
//                 if (causeException != null) {
//                     "message: ${causeException.message} | code: ${causeException.response.status.value} | status: ${causeException.response.status}"
//                 } else {
//                     "message: ${this.message}"
//                 }
//
//             // project specific error mapping
//             return try {
//                 this.toProjectError().message ?: fallbackMessage
//             } catch (e: Exception) {
//                 fallbackMessage
//             }
//         }
//
//         else -> "message: ${this.message ?: ERROR_UNKNOWN}"
//     }
// }
