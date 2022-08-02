package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.domain.models.ServerError
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import retrofit2.Response
import timber.log.Timber

/** Converts a retrofit response to a wrapped [Status] response */
interface ResponseToApiResultMapper {
    /** Convert from response to [Status] of the response */
    fun <T : Any> toResult(response: Response<T>): Status<T>

    /** Convert from response to [Status] of Unit of the response. Use when you don't care about the actual success type. */
    fun <T : Any> toEmptyResult(response: Response<T>): Status<Unit>

    /** Convert from response to [Status] of Unit of the response. Use when you don't care about the actual success type. */
    fun <T : Any> toResponseCode(response: Response<T>): Status<Int>
}

class ResponseToApiResultMapperImpl : ResponseToApiResultMapper {

    override fun <T : Any> toResult(response: Response<T>): Status<T> {
        return when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    Status.Success(body)
                } else {
                    Timber.w("[toResult] Response body null")
                    Status.Failure.GeneralFailure("null response body")
                }
            }
            else -> {
                Timber.w("[toResult] Api not successful: message ${response.message()} code: ${response.code()}")
                Status.Failure.Server(generateServerError(response))
            }
        }
    }

    override fun <T : Any> toEmptyResult(response: Response<T>): Status<Unit> {
        return when {
            response.isSuccessful -> Status.Success(Unit)
            else -> {
                Timber.w("[toEmptyResult] Api not successful: message ${response.message()} code: ${response.code()}")
                Status.Failure.Server(generateServerError(response))
            }
        }
    }

    override fun <T : Any> toResponseCode(response: Response<T>): Status<Int> {
        return when {
            response.isSuccessful -> Status.Success(response.code())
            else -> {
                    Timber.w("[toResponseCode] Api not successful: message ${response.message()} code: ${response.code()}")
                    Status.Failure.Server(generateServerError(response))
                }
        }
    }

    /** Determines the correct error response format, deserializes it, and converts it to a [ServerErrorDto] */
    private fun <T> generateServerError(response: Response<T>): ServerError {
        // Add any custom error response parsing logic here as needed
        return ServerError(httpErrorCode = response.code(), status = response.message())
    }
}
