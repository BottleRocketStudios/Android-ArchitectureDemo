package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.infrastructure.HeaderInterceptorMock
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BasicAuthRepositoryTest {

    @Test
    fun authInterceptor() {
        val auth = BasicAuthRepository()
        runBlocking {
            val interceptor = auth.authInterceptor("patentlychris@gmail.com", "password1")
            val headerInterceptorMock = HeaderInterceptorMock()
            interceptor.intercept(headerInterceptorMock.getMockedChain())
            assert(headerInterceptorMock.headers.size == 1)
            assert(headerInterceptorMock.headers["Authorization"]=="Basic cGF0ZW50bHljaHJpc0BnbWFpbC5jb206cGFzc3dvcmQx")
        }
    }
}