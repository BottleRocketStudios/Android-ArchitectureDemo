package com.bottlerocketstudios.brarchitecture.data.model

import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import timber.log.Timber

/** Converts a network response to a wrapped [Result] response */
interface ResponseToApiResultMapper {
    /** Convert from Ktor HttpResponse to [Result] of the response */
    suspend fun <T : Any> toResult(response: HttpResponse, body: T?): Result<T>

    /**
     * Convert from Ktor HttpResponse to [Result] of Unit of the response. Use when you don't care
     * about the actual success type.
     */
    suspend fun toEmptyResult(response: HttpResponse): Result<Unit>

    /**
     * Convert from Ktor HttpResponse to [Result] of Unit of the response. Use when you don't care
     * about the actual success type.
     */
    suspend fun toResponseCode(response: HttpResponse): Result<Int>
}

class ResponseToApiResultMapperImpl : ResponseToApiResultMapper {

    override suspend fun <T : Any> toResult(response: HttpResponse, body: T?): Result<T> {
        return when {
            response.status.isSuccess() -> {
                if (body != null) {
                    Result.success(body)
                } else {
                    Timber.w("[toResult (Ktor)] Response body null")
                    Result.failure(Exception("null response body"))
                }
            }
            else -> {
                Timber.w(
                        "[toResult (Ktor)] Api not successful: message ${response.status.description} code: ${response.status.value}"
                )
                Result.failure(Exception(response.status.description))
            }
        }
    }

    override suspend fun toEmptyResult(response: HttpResponse): Result<Unit> {
        return when {
            response.status.isSuccess() -> Result.success(Unit)
            else -> {
                Timber.w(
                        "[toEmptyResult (Ktor)] Api not successful: message ${response.status.description} code: ${response.status.value}"
                )
                Result.failure(Exception(response.status.description))
            }
        }
    }

    override suspend fun toResponseCode(response: HttpResponse): Result<Int> {
        return when {
            response.status.isSuccess() -> Result.success(response.status.value)
            else -> {
                Timber.w(
                        "[toResponseCode (Ktor)] Api not successful: message ${response.status.description} code: ${response.status.value}"
                )
                Result.failure(Exception(response.status.description))
            }
        }
    }
}
