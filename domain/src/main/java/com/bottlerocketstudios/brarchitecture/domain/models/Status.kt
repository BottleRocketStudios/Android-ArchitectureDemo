package com.bottlerocketstudios.brarchitecture.domain.models

import java.io.InterruptedIOException
import java.net.SocketException

/**
 * Wrapper class for [Success] type that also represent various [Failure]s with appropriate associated data.
 *
 * Note that a loading type at this level is unnecessary. Loading status can be handled at a higher level if needed but hasn't been very useful from past experience.
 */
sealed class Status<out SUCCESS_TYPE : Any> {

    /** Indicates a successful operation (ex: successful api response) */
    data class Success<out SUCCESS_TYPE : Any>(val data: SUCCESS_TYPE) : Status<SUCCESS_TYPE>()

    /** Indicates a failed operation (ex: api response error, parsing error, exception thrown, etc) */
    sealed class Failure : Status<Nothing>() {
        /** Network failures due to network timeout connection issues (no connection, airplane mode, wifi connected with no internet)  */
        data class NetworkTimeoutFailure(val exception: Exception) : Failure()

        /** Retrofit+Moshi parse failures or other errors related to the api calls */
        data class Server(val error: ServerError?) : Failure()

        /** General failure type bucket */
        data class GeneralFailure(val message: String) : Failure()
    }
}

data class ServerError(
    /** Note that this value is set by the network response rather than being parsed from the backend */
    val httpErrorCode: Int? = null,
    /** Note that this value is set by the work response rather than being parsed from the backend */
    val status: String? = null,
)

/**
 * Executes the [transform] block only when receiver [Status.Success] to modify the wrapped value, similar to Collection/List map function.
 *
 * Note that you need to wrap the value you return back from the [transform] block (at the call site) into an [Status].
 * This gives you the ability to change the type from [Status.Success] to [Status.Failure] based on business logic/etc
 * Ex: Mapping a Dto to DomainModel where the mapping cannot be performed due to invalid data, etc should likely return a [Status.Failure]
 *
 * Note that failure has not been thoroughly tested yet but _should_ work.
 */
fun <T : Any, R : Any> Status<T>.map(transform: (T) -> Status<R>): Status<R> = when (this) {
    is Status.Success -> transform(data)
    is Status.Failure -> this
}

/** Converts ApiResult<T> into ApiResult<Unit> to be used when the success return type doesn't matter */
fun <T : Any> Status<T>.asEmptyResult(): Status<Unit> = when (this) {
    is Status.Success -> Status.Success(Unit)
    is Status.Failure -> this
}

/** Syntax sugar to execute [onSuccessBlock] when this is ApiResult.Success */
inline fun <T : Any> Status<T>.alsoOnSuccess(onSuccessBlock: (T) -> Unit): Status<T> {
    if (this is Status.Success) {
        onSuccessBlock(data)
    }
    return this
}

/** Syntax sugar to wrap the receiver [T] into [Status.Success] and support chaining */
fun <T : Any> T.asSuccess(): Status.Success<T> = Status.Success(this)

/** Resolves exceptions to an appropriate ApiResult.Failure subtype */
fun Exception.asAppropriateFailure(): Status.Failure = when (this) {
    // covers SocketTimeoutException and ConnectTimeoutException
    is InterruptedIOException, is SocketException -> Status.Failure.NetworkTimeoutFailure(this)
    else -> Status.Failure.GeneralFailure(localizedMessage.orEmpty())
}

/**
 * Executes [block], converting any caught exceptions to [Status.Failure.GeneralFailure] with logging
 *
 * @param className Name of calling class (for logging purposes)
 * @param methodName Name of calling function (for logging purposes)
 * @param block Lambda with logic that returns the appropriate [Status] response
 *
 */
// FIXME- make logger interface for domain
suspend fun <T : Any> logWrappedExceptions(className: String, methodName: String, block: suspend () -> Status<T>): Status<T> {
    @Suppress("TooGenericExceptionCaught") // this might be overly broad (catching NPEs isn't ideal) but not worth it yet to check for all other possible types (IO..., Json..., others)
    return try {
        // Timber.tag(className).v("[$methodName]")
        block().also {
            // Timber.tag(className).v("[$methodName] result=$it")
        }
    } catch (e: Exception) {
        e.asAppropriateFailure().also {
            // Timber.tag(className).w(e, "[$methodName wrapExceptions] exception caught and converted to failure: $it")
        }
    }
}

// Simple version of wrap exceptions; with no logging.
suspend fun <T : Any> wrapExceptions(block: suspend () -> Status<T>): Status<T> =
    @Suppress("TooGenericExceptionCaught") // this might be overly broad (catching NPEs isn't ideal) but not worth it yet to check for all other possible types (IO..., Json..., others)
    try { block() } catch (e: Exception) { e.asAppropriateFailure() }
