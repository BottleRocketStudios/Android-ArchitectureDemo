package com.bottlerocketstudios.brarchitecture.data.model

import com.squareup.moshi.Moshi
import retrofit2.Response
import timber.log.Timber

/** Converts a retrofit response to a wrapped [ApiResult] response, handling parsing from the different flavors of error responses to the single [ServerErrorDto] */
interface ResponseToApiResultMapper {
    /** Convert from response to [ApiResult] of the response */
    fun <T : Any> toResult(response: Response<T>): ApiResult<T>
    /** Convert from response to [ApiResult] of Unit of the response. Use when you don't care about the actual success type. */
    fun <T : Any> toEmptyResult(response: Response<T>): ApiResult<Unit>
}

class ResponseToApiResultMapperImplementation(moshi: Moshi) : ResponseToApiResultMapper {

    override fun <T : Any> toResult(response: Response<T>): ApiResult<T> {
        return try {
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        ApiResult.Success(body)
                    } else {
                        Timber.w("[toResult] Response body null")
                        ApiResult.Failure.GeneralFailure("null response body")
                    }
                }
                else -> {
                    Timber.w("[toResult] Api not successful: message ${response.message()} code: ${response.code()}")
                    ApiResult.Failure.Server(generateServerError(response))
                }
            }
        } catch (e: Exception) {
            Timber.w(e, "[toResult] Unknown api error {$e.localizedMessage} stackTrace: {$e.stackTrace}")
            ApiResult.Failure.GeneralFailure(e.localizedMessage ?: "")
        }
    }

    override fun <T : Any> toEmptyResult(response: Response<T>): ApiResult<Unit> {
        return try {
            when {
                response.isSuccessful -> ApiResult.Success(Unit)
                else -> {
                    Timber.w("[toEmptyResult] Api not successful: message ${response.message()} code: ${response.code()}")
                    ApiResult.Failure.Server(generateServerError(response))
                }
            }
        } catch (e: Exception) {
            Timber.w(e, "[toEmptyResult] Unknown api error {$e.localizedMessage} stackTrace: {$e.stackTrace}")
            ApiResult.Failure.GeneralFailure(e.localizedMessage ?: "")
        }
    }

    /** Determines the correct error response format, deserializes it, and converts it to a [ServerErrorDto] */
    private fun <T> generateServerError(response: Response<T>): ServerError {
        // Add any custom error response parsing logic here as needed
        return ServerError(httpErrorCode = response.code(), status = response.message())
    }
}
