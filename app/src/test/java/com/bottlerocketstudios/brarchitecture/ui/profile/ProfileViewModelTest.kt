package com.bottlerocketstudios.brarchitecture.ui.profile

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_DISPLAY_NAME
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_LINK
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_NICKNAME
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest : BaseTest() {
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        runTest {
            MockBitBucketRepo.bitbucketRepository.refreshUser()
        }
        viewModel = ProfileViewModel()
    }

    @Test
    fun avatarUrl_onInitialization_returnsUserAvatarUrl() = runTest {
        viewModel.avatarUrl.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_LINK)
        }
    }

    @Test
    fun displayName_onInitialization_returnsUserDisplayName() = runTest {
        viewModel.displayName.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_DISPLAY_NAME)
        }
    }

    @Test
    fun nickname_onInitialization_returnsUserNickname() = runTest {
        viewModel.nickname.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_NICKNAME)
        }
    }

    @Test
    fun onLogout_onLogoutClicked_shouldReturnUnit() = runTest {
        viewModel.onLogout.test {
            viewModel.onLogoutClicked()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun onLogout_emitValue_shouldReturnUnit() = runTest {
        viewModel.onLogout.test {
            (viewModel.onLogout as? MutableSharedFlow)?.emit(Unit)
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun onLogout_onLogout_onLogoutClicked_repoShouldBeCleared() = runTest {
        viewModel.onLogoutClicked()
        assertThat(MockBitBucketRepo.authenticated).isEqualTo(false)
        assertThat(bitbucketRepository.user.value).isEqualTo(null)
        assertThat(bitbucketRepository.repos.value.isEmpty()).isEqualTo(true)
        assertThat(bitbucketRepository.snippets.value.isEmpty()).isEqualTo(true)
    }
}
