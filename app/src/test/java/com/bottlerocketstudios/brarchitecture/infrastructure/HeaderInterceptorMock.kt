package com.bottlerocketstudios.brarchitecture.infrastructure

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.mockito.Mockito.any


class HeaderInterceptorMock {
    lateinit var requestBuilder : Request.Builder
    fun getMockedChain() : Interceptor.Chain {
        requestBuilder = mock { requestBuilder ->
            on { header(any(), any()) } doAnswer { invocation ->
                requestBuilder
            }
        }
        val request: Request = mock {
            on { newBuilder() } doReturn requestBuilder
        }
        val response: Response = mock {
            on { code() } doReturn 200
        }
        return mock {
            on { request() } doReturn request
            on { proceed(any()) } doReturn response
        }
    }
}