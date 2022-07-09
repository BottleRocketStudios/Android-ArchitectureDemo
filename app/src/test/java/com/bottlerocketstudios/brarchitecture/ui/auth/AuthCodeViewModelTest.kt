package com.bottlerocketstudios.brarchitecture.ui.auth

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.data.BuildConfig
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBuildConfigProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthCodeViewModelTest : BaseTest() {

    // TODO: Test onSignUpClicked external navigation

    private lateinit var viewModel: AuthCodeViewModel
    private val mockBuildConfigProvider = MockBuildConfigProvider

    @Before
    fun setUp() {
        inlineKoinSingle { bitbucketRepository }
        inlineKoinSingle { mockBuildConfigProvider.buildConfigProvider }
        viewModel = AuthCodeViewModel()
    }

    @Test
    fun requestUrl_onLoginClicked_shouldReturnMatchingUri() = runBlocking {
        viewModel.onLoginClicked()

        assertThat(viewModel.requestUrl.value).isEqualTo(
            "https://bitbucket.org/site/oauth2/authorize?client_id=${BuildConfig.BITBUCKET_KEY}&response_type=code"
        )
    }

    @Test
    fun devOptionsEnabled_isDebug_shouldReturnTrue() = runBlocking {
        assertThat(viewModel.devOptionsEnabled).isEqualTo(true)
    }

    // FIXME -- How can I make the devOptionsEnabled reset to false in the same vm?
    @Test
    fun devOptionsEnabled_isProduction_shouldReturnFalse() = runBlocking {
        mockBuildConfigProvider._isDebugOrInternalBuild.value = false
        val viewModel = AuthCodeViewModel()

        assertThat(viewModel.devOptionsEnabled).isEqualTo(false)
    }

    @Test
    fun onDevOptionClicked_devOptionsEventEmit_shouldReturnUnit() = runBlocking {
        viewModel.devOptionsEvent.test {
            viewModel.onDevOptionsClicked()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun onAuthCode_shouldSetRequestUrlToEmptyString() = runBlocking {
        viewModel.onAuthCode("")
        assertThat(viewModel.requestUrl.value.isBlank()).isEqualTo(true)
    }

    @Test
    fun onAuthCode_authenticateReturnsTrue_HomeEventShouldReturnUnit() = runBlocking{
        viewModel.homeEvent.test {
            viewModel.onAuthCode("")
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

}
