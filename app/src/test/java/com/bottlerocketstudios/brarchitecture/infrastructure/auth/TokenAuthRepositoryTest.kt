package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.infrastructure.HeaderInterceptorMock
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TokenAuthRepositoryTest {

    @Test
    fun authInterceptor() {
        System.out.println("Running test")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val auth = TokenAuthRepository(retrofit)
        runBlocking {
            val interceptor = auth.authInterceptor(username = "patentlychris@gmail.com", password = "password1")
            val headerInterceptorMock = HeaderInterceptorMock()
            interceptor.intercept(headerInterceptorMock.getMockedChain())
            assert(headerInterceptorMock.headers.size == 1)
            System.out.println(headerInterceptorMock.headers["Authorization"])
            assert(headerInterceptorMock.headers["Authorization"]=="Basic cGF0ZW50bHljaHJpc0BnbWFpbC5jb206cGFzc3dvcmQx")
        }
    }
}