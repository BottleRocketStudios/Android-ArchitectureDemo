package com.bottlerocketstudios.brarchitecture.ui.auth

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.data.BuildConfig
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBuildConfigProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthCodeViewModelTest : BaseTest() {
    private lateinit var viewModel: AuthCodeViewModel

    @Before
    fun setUp() {
        inlineKoinSingle { MockBuildConfigProvider.DEV }
        viewModel = AuthCodeViewModel()
    }

    @Test
    fun requestUrl_onLoginClicked_shouldReturnMatchingUri() = runTest {
        viewModel.onLoginClicked()

        assertThat(viewModel.requestUrl.value).isEqualTo(
            "https://bitbucket.org/site/oauth2/authorize?client_id=${BuildConfig.BITBUCKET_KEY}&response_type=code"
        )
    }

    @Test
    fun devOptionsEnabled_isDebug_shouldReturnTrue() = runTest {
        assertThat(viewModel.devOptionsEnabled).isEqualTo(true)
    }

    @Test
    fun devOptionsEnabled_isProduction_shouldReturnFalse() = runTest {
        inlineKoinSingle { MockBuildConfigProvider.PROD_RELEASE }
        val model = AuthCodeViewModel()

        assertThat(model.devOptionsEnabled).isEqualTo(false)
    }

    @Test
    fun devOptionsEvent_emitValue_shouldReturnUnit() = runTest {
        viewModel.devOptionsEvent.test {
            (viewModel.devOptionsEvent as? MutableSharedFlow)?.emit(Unit)
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun homeEvent_emitValue_shouldReturnUnit() = runTest {
        viewModel.homeEvent.test {
            (viewModel.homeEvent as? MutableSharedFlow)?.emit(Unit)
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun onDevOptionClicked_devOptionsEventEmit_shouldReturnUnit() = runTest {
        viewModel.devOptionsEvent.test {
            viewModel.onDevOptionsClicked()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun requestUrl_onAuthCodeCalled_shouldReturnEmptyString() = runTest {
        viewModel.onAuthCode("")
        assertThat(viewModel.requestUrl.value.isBlank()).isEqualTo(true)
    }

    @Test
    fun onAuthCode_authenticateReturnsTrue_HomeEventShouldReturnUnit() = runTest {
        viewModel.homeEvent.test {
            viewModel.onAuthCode("")
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }
}
