package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.models.ServerError
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.google.common.truth.Truth.assertThat
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class ResponseToStatusMapperImplTest : BaseTest() {

    @Test
    fun toResult_failureResponse_returnsServerFailure() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode(123, "foo")
        }

        val result = sut.toResult(mockResponse, null as String?)

        assertThat(result).isEqualTo(Status.Failure.Server(ServerError(123, "foo")))
    }

    @Test
    fun toResult_successResponseWithNullBody_returnsGeneralFailure() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode.OK
        }

        val result = sut.toResult(mockResponse, null as String?)

        assertThat(result).isEqualTo(Status.Failure.GeneralFailure("null response body"))
    }

    @Test
    fun toResult_successResponseWithNonNullBody_returnsSuccess() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode.OK
        }

        val result = sut.toResult(mockResponse, "foo")

        assertThat(result).isEqualTo(Status.Success("foo"))
    }

    @Test
    fun toEmptyResult_failureResponse_returnsServerFailure() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode(123, "foo")
        }

        val result = sut.toEmptyResult(mockResponse)

        assertThat(result).isEqualTo(Status.Failure.Server(ServerError(123, "foo")))
    }

    @Test
    fun toEmptyResult_successResponse_returnsSuccess() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode.OK
        }

        val result = sut.toEmptyResult(mockResponse)

        assertThat(result).isEqualTo(Status.Success(Unit))
    }

    @Test
    fun toResponseCode_failureResponse_returnsServerFailure() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode(123, "foo")
        }

        val result = sut.toResponseCode(mockResponse)

        assertThat(result).isEqualTo(Status.Failure.Server(ServerError(123, "foo")))
    }

    @Test
    fun toResponseCode_successResponse_returnsSuccess() = runTest {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<HttpResponse> {
            on { status } doReturn HttpStatusCode.OK
        }

        val result = sut.toResponseCode(mockResponse)

        assertThat(result).isEqualTo(Status.Success(HttpStatusCode.OK.value))
    }
}
