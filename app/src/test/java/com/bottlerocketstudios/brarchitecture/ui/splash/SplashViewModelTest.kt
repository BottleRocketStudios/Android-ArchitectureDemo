package com.bottlerocketstudios.brarchitecture.ui.splash

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SplashViewModelTest : BaseTest() {
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        inlineKoinSingle { MockBitBucketRepo.bitbucketRepository }
        viewModel = SplashViewModel()
    }

    @Test
    fun authEvent_authenticatedIsTrue_shouldEmitUnit() = runBlocking {
        viewModel.authEvent.test {
            MockBitBucketRepo.authenticated = true
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun unAuthEvent_authenticatedIsFalse_shouldEmitUnit() = runBlocking {
        viewModel.unAuthEvent.test {
            MockBitBucketRepo.authenticated = false
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

}
