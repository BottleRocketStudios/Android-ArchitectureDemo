package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response

class ResponseToApiResultMapperImplTest : BaseTest() {

    @Test
    fun toResult_failureResponse_returnsServerFailure() {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<Response<String>> {
            on { isSuccessful } doReturn false
            on { code() } doReturn 123
            on { message() } doReturn "foo"
        }

        val result = sut.toResult(mockResponse)

        assertThat(result).isEqualTo(ApiResult.Failure.Server(ServerError(123, "foo")))
    }

    @Test
    fun toResult_successResponseWithNullBody_returnsGeneralFailure() {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<Response<String>> {
            on { isSuccessful } doReturn true
            on { body() } doReturn null
        }

        val result = sut.toResult(mockResponse)

        assertThat(result).isEqualTo(ApiResult.Failure.GeneralFailure("null response body"))
    }

    @Test
    fun toResult_successResponseWithNonNullBody_returnsSuccess() {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<Response<String>> {
            on { isSuccessful } doReturn true
            on { body() } doReturn "foo"
        }

        val result = sut.toResult(mockResponse)

        assertThat(result).isEqualTo(ApiResult.Success("foo"))
    }

    @Test
    fun toEmptyResult_failureResponse_returnsServerFailure() {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<Response<String>> {
            on { isSuccessful } doReturn false
            on { code() } doReturn 123
            on { message() } doReturn "foo"
        }

        val result = sut.toEmptyResult(mockResponse)

        assertThat(result).isEqualTo(ApiResult.Failure.Server(ServerError(123, "foo")))
    }

    @Test
    fun toResult_successResponseWithNullBody_returnsSuccess() {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<Response<String>> {
            on { isSuccessful } doReturn true
            on { body() } doReturn null
        }

        val result = sut.toEmptyResult(mockResponse)

        assertThat(result).isEqualTo(ApiResult.Success(Unit))
    }

    @Test
    fun toEmptyResult_successResponseWithNonNullBody_returnsSuccess() {
        val sut = ResponseToApiResultMapperImpl()
        val mockResponse = mock<Response<String>> {
            on { isSuccessful } doReturn true
            on { body() } doReturn "foo"
        }

        val result = sut.toEmptyResult(mockResponse)

        assertThat(result).isEqualTo(ApiResult.Success(Unit))
    }
}
