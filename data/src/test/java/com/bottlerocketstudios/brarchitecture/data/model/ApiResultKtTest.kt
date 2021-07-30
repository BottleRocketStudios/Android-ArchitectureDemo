package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import timber.log.Timber
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketException

class ApiResultKtTest : BaseTest() {

    @Test
    fun map_apiResultSuccess_returnsMappedApiResultSuccess() {
        val sut = ApiResult.Success("original")
        val result = sut.map { ApiResult.Success("mapped") }
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo("mapped")
    }

    @Test
    fun map_apiResultFailure_passthroughSameApiFailureResult() {
        val sut = ApiResult.Failure.GeneralFailure("original")
        val result = sut.map { ApiResult.Success("mapped") }
        assertThat(result).isInstanceOf(ApiResult.Failure::class.java)
        assertThat((result as ApiResult.Failure)).isEqualTo(sut)
    }

    @Test
    fun asEmptyResult_apiResultSuccess_returnsUnitWrappedInApiResultSuccess() {
        val sut = ApiResult.Success("original")
        val result = sut.asEmptyResult()
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat(result).isEqualTo(ApiResult.Success(Unit))
    }

    @Test
    fun asEmptyResult_apiResultFailure_passthroughSameApiFailureResult() {
        val sut = ApiResult.Failure.GeneralFailure("original")
        val result = sut.asEmptyResult()
        assertThat(result).isInstanceOf(ApiResult.Failure::class.java)
        assertThat((result as ApiResult.Failure)).isEqualTo(sut)
    }

    @Test
    fun alsoOnSuccess_apiResultSuccess_passthroughExistingApiResultSuccess() {
        val sut = ApiResult.Success("original")
        val successLambda: (String) -> Unit = { data ->
            Timber.v("data is $data")
        }
        val mockSuccessLambda = mock<(String) -> Unit> {
            successLambda::class.java
        }
        val result = sut.alsoOnSuccess(mockSuccessLambda)
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat(result).isEqualTo(sut)
        verify(mockSuccessLambda).invoke(sut.data)
    }

    @Test
    fun alsoOnSuccess_apiResultFailure_passthroughSameApiFailureResult() {
        val sut = ApiResult.Failure.GeneralFailure("original")
        val successLambda: (String) -> Unit = { data ->
            Timber.v("data is $data")
        }
        val mockSuccessLambda = mock<(String) -> Unit> {
            successLambda::class.java
        }
        val result = sut.alsoOnSuccess(mockSuccessLambda)
        assertThat(result).isInstanceOf(ApiResult.Failure::class.java)
        assertThat(result).isEqualTo(sut)
        verify(mockSuccessLambda, never()).invoke(any())
    }

    @Test
    fun asSuccess_givenString_returnsStringWrappedInApiResultSuccess() {
        val sut = "original"
        val result = sut.asSuccess()
        assertThat(result).isEqualTo(ApiResult.Success(sut))
    }

    @Test
    fun asAppropriateFailure_givenInterruptedIOException_returnsNetworkTimeoutFailure() {
        val sut = InterruptedIOException()
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(ApiResult.Failure.NetworkTimeoutFailure(sut))
    }

    @Test
    fun asAppropriateFailure_givenSocketException_returnsNetworkTimeoutFailure() {
        val sut = SocketException()
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(ApiResult.Failure.NetworkTimeoutFailure(sut))
    }

    @Test
    fun asAppropriateFailure_givenOtherExceptionWithMessage_returnsGeneralFailureWithMessage() {
        val sut = IOException("foo")
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(ApiResult.Failure.GeneralFailure("foo"))
    }

    @Test
    fun asAppropriateFailure_givenOtherExceptionWithNoMessage_returnsGeneralFailureWithEmptyMessage() {
        val sut = IOException()
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(ApiResult.Failure.GeneralFailure(""))
    }

    @Test
    fun wrapExceptions_apiResultSuccess_returnsApiResultSuccess() = runBlocking {
        val blockLambda: suspend () -> ApiResult<String> = {
            ApiResult.Success("baz")
        }
        val result = wrapExceptions("FooClass", "barMethod", blockLambda)
        // TODO: Possibly use mockito mocks to verify that blockLambda is invoked. Attempts fail currently end up with a null result so removed for now.
        assertThat(result).isEqualTo(ApiResult.Success("baz"))
    }

    @Test
    fun wrapExceptions_exceptionInBlockLambda_returnsApiResultFailure() = runBlocking {
        val blockLambda: suspend () -> ApiResult<String> = {
            error("exception in processing")
        }
        val result = wrapExceptions("FooClass", "barMethod", blockLambda)
        // TODO: Possibly use mockito mocks to verify that blockLambda is invoked. Attempts fail currently end up with a null result so removed for now.
        assertThat(result).isInstanceOf(ApiResult.Failure::class.java)
    }
}
