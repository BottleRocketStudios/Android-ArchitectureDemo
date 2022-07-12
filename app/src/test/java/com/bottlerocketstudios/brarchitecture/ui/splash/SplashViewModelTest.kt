package com.bottlerocketstudios.brarchitecture.ui.splash

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SplashViewModelTest : BaseTest() {

    @Test
    fun authEvent_authenticatedIsTrue_shouldEmitUnit() = runBlocking {
        MockBitBucketRepo.authenticated = true

        val viewModel = SplashViewModel()
        viewModel.authEvent.test {
            viewModel.authenticate()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun unAuthEvent_authenticatedIsFalse_shouldEmitUnit() = runBlocking {
        MockBitBucketRepo.authenticated = false

        val viewModel = SplashViewModel()
        viewModel.unAuthEvent.test {
            viewModel.authenticate()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }
}
