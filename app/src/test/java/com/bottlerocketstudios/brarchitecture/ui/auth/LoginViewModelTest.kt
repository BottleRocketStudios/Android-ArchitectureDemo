package com.bottlerocketstudios.brarchitecture.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
        val loginViewModel = LoginViewModel(mock {}, mock {}, mock {}, dispatcherProvider)
        loginViewModel.email.postValue("test@example.com")
        loginViewModel.password.postValue("Password1!")
        assertThat(loginViewModel.loginEnabled.value).isTrue()
    }

    @Test
    fun loginViewModel_shouldDisableLogin_withInvalidCredentials() {
        val loginViewModel = LoginViewModel(mock {}, mock {}, mock {}, dispatcherProvider)
        loginViewModel.email.postValue("t")
        loginViewModel.password.postValue("P")
        assertThat(loginViewModel.loginEnabled.value).isFalse()
    }

    private fun tryToLoginWithMockedRepo(repo: BitbucketRepository): Boolean? {
        val loginViewModel = LoginViewModel(mock {}, repo, mock {}, dispatcherProvider)
        loginViewModel.email.postValue("test@example.com")
        loginViewModel.password.postValue("Password1!")
        loginViewModel.onLoginClicked()
        return loginViewModel.authenticated.value
    }

    @Test
    fun loginViewModel_shouldSetAuthenticatedToTrue_whenRepoSaysTo() {
        val repo: BitbucketRepository = mock {
            onBlocking {
                authenticate(any())
            } doReturn true
        }
        val authenticated = tryToLoginWithMockedRepo(repo)
        assertThat(authenticated).isTrue()
    }

    @Test
    fun loginViewModel_shouldSetAuthenticatedToFalse_whenRepoSaysTo() {
        val repo: BitbucketRepository = mock {
            onBlocking {
                authenticate(any())
            } doReturn false
        }
        val authenticated = tryToLoginWithMockedRepo(repo)
        assertThat(authenticated).isFalse()
    }

    @Test
    fun loginViewModel_shouldClearObservers_whenDoClearCalled() {
        val loginViewModel = LoginViewModel(mock {}, mock {}, mock {}, dispatcherProvider)
        assertThat(loginViewModel.password.hasActiveObservers()).isTrue()
        assertThat(loginViewModel.email.hasActiveObservers()).isTrue()
        assertThat(loginViewModel.textWatcher).isNotNull()
        loginViewModel.doClear()
        assertThat(loginViewModel.password.hasActiveObservers()).isFalse()
        assertThat(loginViewModel.email.hasActiveObservers()).isFalse()
    }
}
