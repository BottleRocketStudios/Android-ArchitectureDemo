package com.bottlerocketstudios.brarchitecture.data.model

import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketException

/**
 * Wrapper class for [Success] type that also represent various [Failure]s with appropriate associated data.
 *
 * Note that a loading type at this level is unnecessary. Loading status can be handled at a higher level if needed but hasn't been very useful from past experience.
 */
sealed class ApiResult<out SUCCESS_TYPE : Any> {

    /** Indicates a successful operation (ex: successful api response) */
    data class Success<out SUCCESS_TYPE : Any>(val data: SUCCESS_TYPE) : ApiResult<SUCCESS_TYPE>()

    /** Indicates a failed operation (ex: api response error, parsing error, exception thrown, etc) */
    sealed class Failure : ApiResult<Nothing>() {
        /** Network failures due to network timeout connection issues (no connection, airplane mode, wifi connected with no internet)  */
        data class NetworkTimeoutFailure(val exception: Exception) : Failure()

        /** Retrofit+Moshi parse failures or other errors related to the api calls */
        data class Server(val error: ServerError?) : Failure()

        /** General failure type bucket */
        data class GeneralFailure(val message: String) : Failure()
    }
}

/**
 * Executes the [transform] block only when receiver [ApiResult.Success] to modify the wrapped value, similar to Collection/List map function.
 *
 * Note that you need to wrap the value you return back from the [transform] block (at the call site) into an [ApiResult].
 * This gives you the ability to change the type from [ApiResult.Success] to [ApiResult.Failure] based on business logic/etc
 * Ex: Mapping a Dto to DomainModel where the mapping cannot be performed due to invalid data, etc should likely return a [ApiResult.Failure]
 *
 * Note that failure has not been thoroughly tested yet but _should_ work.
 */
fun <T : Any, R : Any> ApiResult<T>.map(transform: (T) -> ApiResult<R>): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> transform(data)
        is ApiResult.Failure -> this
    }
}

/** Converts ApiResult<T> into ApiResult<Unit> to be used when the success return type doesn't matter */
fun <T : Any> ApiResult<T>.asEmptyResult(): ApiResult<Unit> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(Unit)
        is ApiResult.Failure -> this
    }
}

/** Syntax sugar to execute [onSuccessBlock] when this is ApiResult.Success */
inline fun <T : Any> ApiResult<T>.alsoOnSuccess(onSuccessBlock: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        onSuccessBlock(data)
    }
    return this
}

/** Syntax sugar to wrap the receiver [T] into [ApiResult.Success] and support chaining */
fun <T : Any> T.asSuccess(): ApiResult.Success<T> = ApiResult.Success(this)

/** Resolves exceptions to an appropriate ApiResult.Failure subtype */
fun Exception.asAppropriateFailure(): ApiResult.Failure {
    return when {
        // covers SocketTimeoutException and ConnectTimeoutException
        this is InterruptedIOException ||
            // covers multiple applicable exceptions including ConnectException
            this is SocketException -> {
            ApiResult.Failure.NetworkTimeoutFailure(this)
        }
        else -> ApiResult.Failure.GeneralFailure(localizedMessage.orEmpty())
    }
}

/**
 * Executes [block], converting any caught exceptions to [ApiResult.Failure.GeneralFailure] with logging
 *
 * @param className Name of calling class (for logging purposes)
 * @param methodName Name of calling function (for logging purposes)
 * @param block Lambda with logic that returns the appropriate [ApiResult] response
 *
 */
suspend fun <T : Any> wrapExceptions(className: String, methodName: String, block: suspend () -> ApiResult<T>): ApiResult<T> {
    return try {
        Timber.tag(className).v("[$methodName]")
        block().also { Timber.tag(className).v("[$methodName] result=$it") }
    } catch (e: Exception) {
        e.asAppropriateFailure().also { Timber.tag(className).w(e, "[$methodName wrapExceptions] exception caught and converted to failure: $it") }
    }
}
