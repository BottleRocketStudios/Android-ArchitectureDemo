package com.bottlerocketstudios.brarchitecture.ui.profile

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_DISPLAY_NAME
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_LINK
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_NICKNAME
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest: BaseTest() {

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        runTest {
            MockBitBucketRepo.bitbucketRepository.refreshUser()
        }
        inlineKoinSingle { MockBitBucketRepo.bitbucketRepository }
        viewModel = ProfileViewModel()
    }

    @Test
    fun avatarUrl_onInitialization_returnsUserAvatarUrl() = runBlocking {
        viewModel.avatarUrl.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_LINK)
        }
    }

    @Test
    fun displayName_onInitialization_returnsUserDisplayName() = runBlocking {
        viewModel.displayName.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_DISPLAY_NAME)
        }
    }

    @Test
    fun nickname_onInitialization_returnsUserNickname() = runBlocking {
        viewModel.nickname.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_NICKNAME)
        }
    }

    @Test
    fun onLogout_whenOnLogoutClicked_shouldReturnUnit() = runBlocking {
        viewModel.onLogout.test {
            viewModel.onLogoutClicked()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }
}
