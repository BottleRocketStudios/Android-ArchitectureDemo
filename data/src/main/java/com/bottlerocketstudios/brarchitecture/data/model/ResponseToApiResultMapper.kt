package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.domain.models.ServerError
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import timber.log.Timber

/** Converts a network response to a wrapped [Status] response */
interface ResponseToApiResultMapper {
    /** Convert from Ktor HttpResponse to [Status] of the response */
    suspend fun <T : Any> toResult(response: HttpResponse, body: T?): Status<T>

    /** Convert from Ktor HttpResponse to [Status] of Unit of the response. Use when you don't care about the actual success type. */
    suspend fun toEmptyResult(response: HttpResponse): Status<Unit>

    /** Convert from Ktor HttpResponse to [Status] of Unit of the response. Use when you don't care about the actual success type. */
    suspend fun toResponseCode(response: HttpResponse): Status<Int>
}

class ResponseToApiResultMapperImpl : ResponseToApiResultMapper {

    override suspend fun <T : Any> toResult(response: HttpResponse, body: T?): Status<T> {
        return when {
            response.status.isSuccess() -> {
                if (body != null) {
                    Status.Success(body)
                } else {
                    Timber.w("[toResult (Ktor)] Response body null")
                    Status.Failure.GeneralFailure("null response body")
                }
            }
            else -> {
                Timber.w("[toResult (Ktor)] Api not successful: message ${response.status.description} code: ${response.status.value}")
                Status.Failure.Server(generateServerError(response))
            }
        }
    }

    override suspend fun toEmptyResult(response: HttpResponse): Status<Unit> {
        return when {
            response.status.isSuccess() -> Status.Success(Unit)
            else -> {
                Timber.w("[toEmptyResult (Ktor)] Api not successful: message ${response.status.description} code: ${response.status.value}")
                Status.Failure.Server(generateServerError(response))
            }
        }
    }

    override suspend fun toResponseCode(response: HttpResponse): Status<Int> {
        return when {
            response.status.isSuccess() -> Status.Success(response.status.value)
            else -> {
                Timber.w("[toResponseCode (Ktor)] Api not successful: message ${response.status.description} code: ${response.status.value}")
                Status.Failure.Server(generateServerError(response))
            }
        }
    }

    private fun generateServerError(response: HttpResponse): ServerError {
        return ServerError(httpErrorCode = response.status.value, status = response.status.description)
    }
}
