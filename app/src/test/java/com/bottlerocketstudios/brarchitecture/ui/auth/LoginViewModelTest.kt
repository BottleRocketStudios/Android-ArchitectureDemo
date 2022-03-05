package com.bottlerocketstudios.brarchitecture.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.SetDispatcherOnMain
import com.bottlerocketstudios.brarchitecture.test.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginViewModelTest : BaseTest() {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcherProvider = TestDispatcherProvider()

    @get:Rule
    val dispatcherRule = SetDispatcherOnMain(dispatcherProvider.Unconfined)

    @Test
    fun loginViewModel_shouldEnableLogin_withValidCredentials() = runBlockingTest {
        val sut = LoginViewModel(mock {}, mock {}, mock {})
        sut.email.value = "test@example.com"
        sut.password.value = "Password1!"

        sut.loginEnabled.test {
            assertThat(awaitItem()).isTrue()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun loginViewModel_shouldDisableLogin_withInvalidCredentials() = runBlockingTest {
        val sut = LoginViewModel(mock {}, mock {}, mock {})
        sut.email.value = "t"
        sut.password.value = "P"

        sut.loginEnabled.test {

            assertThat(awaitItem()).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }
}
