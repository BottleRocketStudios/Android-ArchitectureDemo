package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.HeaderInterceptorMock
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.token.TokenAuthService
import com.google.common.truth.Truth.assertWithMessage
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class TokenAuthInterceptorTest : BaseTest() {

    @Test
    fun authInterceptor() {
        var accessToken: AccessToken? = null
        val response: Response<*> = mock() {
            on { body() }.then { accessToken }
        }
        val call: Call<AccessToken> = mock() {
            on { execute() }.then { response }
        }
        val service: TokenAuthService = mock() {
            on { getToken(any(), any(), any(), any()) }.doAnswer { invocation ->
                accessToken = AccessToken(access_token = "${invocation.getArgument<String>(0)} + ${invocation.getArgument<String>(1)}")
                call
            }
        }
        val bitbucketCredentialsRepository = mock<BitbucketCredentialsRepository> {
            on { loadCredentials() } doReturn ValidCredentialModel("patentlychris@gmail.com", "password1")
            on { loadToken() } doAnswer { accessToken }
        }
        val interceptor = TokenAuthInterceptor(service, bitbucketCredentialsRepository)
        runBlocking {
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
