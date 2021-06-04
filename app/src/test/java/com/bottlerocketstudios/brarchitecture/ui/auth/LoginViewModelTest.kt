package com.bottlerocketstudios.brarchitecture.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class LoginViewModelTest : BaseTest() {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcherProvider = TestDispatcherProvider()

    @Test
    fun loginViewModel_shouldEnableLogin_withValidCredentials() {
        val loginViewModel = LoginViewModel(mock {}, mock {}, mock {}, mock {}, dispatcherProvider)
        loginViewModel.email.postValue("test@example.com")
        loginViewModel.password.postValue("Password1!")
        assertThat(loginViewModel.loginEnabled.value).isTrue()
    }

    @Test
    fun loginViewModel_shouldDisableLogin_withInvalidCredentials() {
        val loginViewModel = LoginViewModel(mock {}, mock {}, mock {}, mock {}, dispatcherProvider)
        loginViewModel.email.postValue("t")
        loginViewModel.password.postValue("P")
        assertThat(loginViewModel.loginEnabled.value).isFalse()
    }

    @Test
    fun loginViewModel_shouldClearObservers_whenDoClearCalled() {
        val loginViewModel = LoginViewModel(mock {}, mock {}, mock {}, mock {}, dispatcherProvider)
        assertThat(loginViewModel.password.hasActiveObservers()).isTrue()
        assertThat(loginViewModel.email.hasActiveObservers()).isTrue()
        assertThat(loginViewModel.textWatcher).isNotNull()
        loginViewModel.doClear()
        assertThat(loginViewModel.password.hasActiveObservers()).isFalse()
        assertThat(loginViewModel.email.hasActiveObservers()).isFalse()
    }
}
