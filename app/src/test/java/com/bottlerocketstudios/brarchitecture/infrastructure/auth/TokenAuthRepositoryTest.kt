package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.infrastructure.HeaderInterceptorMock
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body

class TokenAuthRepositoryTest {

    @Test
    fun authInterceptor() {
        // The Retrofit required to actually make the call
        /*
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            */
        val retrofit = mock(Retrofit::class.java)
        val service = mock(TokenAuthRepository.AuthService::class.java)
        `when`(service.getToken(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer { invocation ->
            val call = mock(Call::class.java)
            val response = mock(Response::class.java)
            val body = mock(Body::class.java)
            val accessToken = TokenAuthRepository.AccessToken()
            accessToken.access_token = "${invocation.getArgument<String>(0)} + ${invocation.getArgument<String>(1)}"
            `when`(response.body()).then {accessToken}
            `when`(call.execute()).then {response}
            call
        }
        `when`(retrofit.create(Mockito.any(Class::class.java))).then { service }
        val auth = TokenAuthRepository(retrofit)
        runBlocking {
            val interceptor = auth.authInterceptor(username = "patentlychris@gmail.com", password = "password1")
            val headerInterceptorMock = HeaderInterceptorMock()
            interceptor.intercept(headerInterceptorMock.getMockedChain())
            assert(headerInterceptorMock.headers.size == 1)
            System.out.println(headerInterceptorMock.headers["Authorization"])
            assert(headerInterceptorMock.headers["Authorization"]=="Bearer patentlychris@gmail.com + password1")
        }
    }
}