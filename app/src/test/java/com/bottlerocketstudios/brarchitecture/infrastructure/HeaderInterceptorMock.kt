package com.bottlerocketstudios.brarchitecture.infrastructure

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class HeaderInterceptorMock {
    val headers = mutableMapOf<String, String>()
    fun getMockedChain() : Interceptor.Chain {
        val chain = mock(Interceptor.Chain::class.java)
        val request = mock(Request::class.java)
        val requestBuilder = mock(Request.Builder::class.java)
        Mockito.`when`(requestBuilder.header(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer { invocation ->
            headers.put(invocation.getArgument(0), invocation.getArgument(1))
            requestBuilder
        }
        `when`(request.newBuilder()).then {requestBuilder}
        `when`(chain.request()).then {request}
        val response = mock(Response::class.java)
        `when`(response.code()).then {200}
        `when`(chain.proceed(ArgumentMatchers.any())).then {response}
        return chain
    }
}