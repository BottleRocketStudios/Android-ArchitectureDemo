package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.alsoOnSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.asAppropriateFailure
import com.bottlerocketstudios.brarchitecture.domain.models.asEmptyResult
import com.bottlerocketstudios.brarchitecture.domain.models.asSuccess
import com.bottlerocketstudios.brarchitecture.domain.models.logWrappedExceptions
import com.bottlerocketstudios.brarchitecture.domain.models.map
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import timber.log.Timber
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketException

class StatusKtTest : BaseTest() {

    @Test
    fun map_apiResultSuccess_returnsMappedApiResultSuccess() {
        val sut = Status.Success("original")
        val result = sut.map { Status.Success("mapped") }
        assertThat(result).isInstanceOf(Status.Success::class.java)
        assertThat((result as Status.Success).data).isEqualTo("mapped")
    }

    @Test
    fun map_apiResultFailure_passthroughSameApiFailureResult() {
        val sut = Status.Failure.GeneralFailure("original")
        val result = sut.map { Status.Success("mapped") }
        assertThat(result).isInstanceOf(Status.Failure::class.java)
        assertThat((result as Status.Failure)).isEqualTo(sut)
    }

    @Test
    fun asEmptyResult_apiResultSuccess_returnsUnitWrappedInApiResultSuccess() {
        val sut = Status.Success("original")
        val result = sut.asEmptyResult()
        assertThat(result).isInstanceOf(Status.Success::class.java)
        assertThat(result).isEqualTo(Status.Success(Unit))
    }

    @Test
    fun asEmptyResult_apiResultFailure_passthroughSameApiFailureResult() {
        val sut = Status.Failure.GeneralFailure("original")
        val result = sut.asEmptyResult()
        assertThat(result).isInstanceOf(Status.Failure::class.java)
        assertThat((result as Status.Failure)).isEqualTo(sut)
    }

    @Test
    fun alsoOnSuccess_apiResultSuccess_passthroughExistingApiResultSuccess() {
        val sut = Status.Success("original")
        val successLambda: (String) -> Unit = { data ->
            Timber.v("data is $data")
        }
        val mockSuccessLambda = mock<(String) -> Unit> {
            successLambda::class.java
        }
        val result = sut.alsoOnSuccess(mockSuccessLambda)
        assertThat(result).isInstanceOf(Status.Success::class.java)
        assertThat(result).isEqualTo(sut)
        verify(mockSuccessLambda).invoke(sut.data)
    }

    @Test
    fun alsoOnSuccess_apiResultFailure_passthroughSameApiFailureResult() {
        val sut = Status.Failure.GeneralFailure("original")
        val successLambda: (String) -> Unit = { data ->
            Timber.v("data is $data")
        }
        val mockSuccessLambda = mock<(String) -> Unit> {
            successLambda::class.java
        }
        val result = sut.alsoOnSuccess(mockSuccessLambda)
        assertThat(result).isInstanceOf(Status.Failure::class.java)
        assertThat(result).isEqualTo(sut)
        verify(mockSuccessLambda, never()).invoke(any())
    }

    @Test
    fun asSuccess_givenString_returnsStringWrappedInApiResultSuccess() {
        val sut = "original"
        val result = sut.asSuccess()
        assertThat(result).isEqualTo(Status.Success(sut))
    }

    @Test
    fun asAppropriateFailure_givenInterruptedIOException_returnsNetworkTimeoutFailure() {
        val sut = InterruptedIOException()
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(Status.Failure.NetworkTimeoutFailure(sut))
    }

    @Test
    fun asAppropriateFailure_givenSocketException_returnsNetworkTimeoutFailure() {
        val sut = SocketException()
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(Status.Failure.NetworkTimeoutFailure(sut))
    }

    @Test
    fun asAppropriateFailure_givenOtherExceptionWithMessage_returnsGeneralFailureWithMessage() {
        val sut = IOException("foo")
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(Status.Failure.GeneralFailure("foo"))
    }

    @Test
    fun asAppropriateFailure_givenOtherExceptionWithNoMessage_returnsGeneralFailureWithEmptyMessage() {
        val sut = IOException()
        val result = sut.asAppropriateFailure()
        assertThat(result).isEqualTo(Status.Failure.GeneralFailure(""))
    }

    @Test
    fun wrapExceptions_apiResultSuccess_returnsApiResultSuccess() = runTest {
        val blockLambda: suspend () -> Status<String> = {
            Status.Success("baz")
        }
        val result = logWrappedExceptions("FooClass", "barMethod", blockLambda)
        // TODO: Possibly use mockito mocks to verify that blockLambda is invoked. Attempts fail currently end up with a null result so removed for now.
        assertThat(result).isEqualTo(Status.Success("baz"))
    }

    @Test
    fun wrapExceptions_exceptionInBlockLambda_returnsApiResultFailure() = runTest {
        val blockLambda: suspend () -> Status<String> = {
            error("exception in processing")
        }
        val result = logWrappedExceptions("FooClass", "barMethod", blockLambda)
        // TODO: Possibly use mockito mocks to verify that blockLambda is invoked. Attempts fail currently end up with a null result so removed for now.
        assertThat(result).isInstanceOf(Status.Failure::class.java)
    }
}
