package com.bottlerocketstudios.brarchitecture.ui.splash

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.generateTestDispatcherProvider
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SplashViewModelTest : BaseTest() {

    // Using StandardTestDispatcher to allow testing of emissions sent during ViewModel initialization since doesn't eagerly run coroutines
    override val testDispatcherProvider = StandardTestDispatcher().generateTestDispatcherProvider()

    @Test
    fun authEvent_authenticatedIsTrue_shouldEmitUnit() = runTest {
        MockBitBucketRepo.authenticated = true

        val viewModel = SplashViewModel()
        viewModel.authEvent.test {
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun unAuthEvent_authenticatedIsFalse_shouldEmitUnit() = runTest {
        MockBitBucketRepo.authenticated = false

        val viewModel = SplashViewModel()
        viewModel.unAuthEvent.test {
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }
}
