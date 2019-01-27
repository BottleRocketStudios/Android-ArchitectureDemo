package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.infrastructure.HeaderInterceptorMock
import com.google.common.truth.Truth.assertWithMessage
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

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
        val accessToken = TokenAuthRepository.AccessToken()
        val response: Response<*> = mock() {
            on {body()}.then {accessToken}
        }
        val call: Call<TokenAuthRepository.AccessToken> = mock() {
            on {execute()}.then { response }
        }
        val service: TokenAuthRepository.AuthService = mock() {
            on {getToken(any(), any(), any(), any())}.doAnswer { invocation ->
                accessToken.access_token = "${invocation.getArgument<String>(0)} + ${invocation.getArgument<String>(1)}"
                call
            }
        }
        val retrofit: Retrofit = mock() {
            on {create<Any>(any())}.then {service}
        }
        val auth = TokenAuthRepository(retrofit)
        runBlocking {
            val interceptor = auth.authInterceptor(username = "patentlychris@gmail.com", password = "password1")
            val headerInterceptorMock = HeaderInterceptorMock()
            interceptor.intercept(headerInterceptorMock.getMockedChain())
            // Need to capture two arguments, can't use mockito-kotlin dsl
            val nameCaptor = argumentCaptor<String>()
            val valueCaptor = argumentCaptor<String>()
            verify(headerInterceptorMock.requestBuilder, times(1)).header(nameCaptor.capture(), valueCaptor.capture())
            assertWithMessage("Header should be added with key 'Authorization'")
                .that(nameCaptor.lastValue)
                .isEqualTo("Authorization")
            assertWithMessage("Header value should be created based on username and password")
                .that(valueCaptor.lastValue)
                .isEqualTo("Bearer patentlychris@gmail.com + password1")
        }
    }
}