package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.HeaderInterceptorMock
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.basic.BasicAuthInterceptor
import com.google.common.truth.Truth.assertWithMessage
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BasicAuthInterceptorTest : BaseTest() {

    @Test
    fun authInterceptor() {
        runBlocking {
            val bitbucketCredentialsRepository = mock<BitbucketCredentialsRepository> {
                on { loadCredentials() } doReturn ValidCredentialModel("patentlychris@gmail.com", "password1")
            }
            val interceptor = BasicAuthInterceptor(bitbucketCredentialsRepository)

            val headerInterceptorMock = HeaderInterceptorMock()
            interceptor.intercept(headerInterceptorMock.getMockedChain())
            // Need to capture two arguments, can't use mockito-kotlin dsl
            val nameCaptor = argumentCaptor<String>()
            val valueCaptor = argumentCaptor<String>()
            verify(headerInterceptorMock.requestBuilder, times(1)).header(nameCaptor.capture(), valueCaptor.capture())
            assertWithMessage("Header should be added with key 'Authorization'")
                .that(nameCaptor.lastValue)
                .isEqualTo("Authorization")
            assertWithMessage("Header value should be base64 encoding of username and password")
                .that(valueCaptor.lastValue)
                .isEqualTo("Basic cGF0ZW50bHljaHJpc0BnbWFpbC5jb206cGFzc3dvcmQx")
        }
    }
}
