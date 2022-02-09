package com.bottlerocketstudios.brarchitecture.data.network

import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.mockito.Mockito.any

class HeaderInterceptorMock {
    lateinit var requestBuilder: Request.Builder
    fun getMockedChain(): Interceptor.Chain {
        requestBuilder = mock { requestBuilder ->
            on { header(any(), any()) } doAnswer { _ ->
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
