package com.bottlerocketstudios.brarchitecture.data.network.auth

import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.model.toProtectedProperty
import com.bottlerocketstudios.brarchitecture.data.network.HeaderInterceptorMock
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertWithMessage
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class TokenAuthInterceptorTest : BaseTest() {

    @Test
    fun authInterceptor() {
        var accessToken: AccessToken? = null
        val response: Response<*> = mock {
            on { body() }.then { accessToken }
        }
        val call: Call<AccessToken> = mock {
            on { execute() }.then { response }
        }
        val service: TokenAuthService = mock {
            on { getToken(any(), any(), any(), any()) }.doAnswer { invocation ->
                accessToken = AccessToken(accessToken = "${invocation.getArgument<String>(0)} + ${invocation.getArgument<String>(1)}".toProtectedProperty())
                call
            }
        }
        val bitbucketCredentialsRepository = mock<BitbucketCredentialsRepository> {
            on { loadCredentials() } doReturn ValidCredentialModel("test@example.com".toProtectedProperty(), "password1".toProtectedProperty())
            on { loadToken() } doAnswer { accessToken }
        }
        val interceptor = TokenAuthInterceptor(service, bitbucketCredentialsRepository)
        runTest {
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
                .isEqualTo("Bearer test@example.com + password1")
        }
    }
}
