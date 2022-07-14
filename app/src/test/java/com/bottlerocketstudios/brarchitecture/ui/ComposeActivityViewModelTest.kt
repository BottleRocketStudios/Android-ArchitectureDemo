package com.bottlerocketstudios.brarchitecture.ui

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBuildConfigProvider
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_DISPLAY_NAME
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_LINK
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_NAME
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ComposeActivityViewModelTest : BaseTest() {

    private lateinit var viewModel: ComposeActivityViewModel

    @Before
    fun setUp() {
        runTest {
            bitbucketRepository.refreshMyRepos()
            bitbucketRepository.refreshUser()
        }
        inlineKoinSingle { MockBuildConfigProvider.DEV }
        viewModel = ComposeActivityViewModel()
    }

    @Test
    fun title_initValue_shouldReturnEmptyString() {
        assertThat(viewModel.title.value).isEqualTo("")
    }

    @Test
    fun showToolbar_initValue_shouldReturnFalse() = runTest {
        viewModel.showToolbar.test {
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun topLevel_initValue_shouldReturnEmptyString() = runTest {
        viewModel.topLevel.test {
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun selectedRepo_initValue_shouldReturnEmptyRepo() = runTest {
        viewModel.selectedRepo.test {
            assertThat(awaitItem())
                .isEqualTo(GitRepository(null, null, null, null, null, null, null))
        }
    }

    @Test
    fun devOptionsEnabled_initValue_shouldReturnTrue() = runTest {
        assertThat(viewModel.devOptionsEnabled).isTrue()
    }

    @Test
    fun avatarUrl_initWithValue_shouldReturnAvatarUrl() = runTest {
        viewModel.avatarUrl.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_LINK)
        }
    }

    @Test
    fun displayName_initWithValue_shouldReturnDisplayName() = runTest {
        viewModel.displayName.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_DISPLAY_NAME)
        }
    }

    @Test
    fun userName_initWithValue_shouldReturnUserName() = runTest {
        viewModel.username.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_NAME)
        }
    }
}
