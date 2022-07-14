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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    fun showToolbar_initValue_shouldReturnFalse() = runBlocking {
        viewModel.showToolbar.test {
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun topLevel_initValue_shouldReturnEmptyString() = runBlocking {
        viewModel.topLevel.test {
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun selectedRepo_initValue_shouldReturnEmptyRepo() = runBlocking {
        viewModel.selectedRepo.test {
            assertThat(awaitItem())
                .isEqualTo(GitRepository(null, null, null, null, null, null, null))
        }
    }

    @Test
    fun devOptionsEnabled_initValue_shouldReturnTrue() = runBlocking {
        assertThat(viewModel.devOptionsEnabled).isTrue()
    }

    @Test
    fun avatarUrl_initWithValue_shouldReturnAvatarUrl() = runBlocking {
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.avatarUrl.collect() }
        viewModel.avatarUrl.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_LINK)
        }
        collector.cancel()
    }

    @Test
    fun displayName_initWithValue_shouldReturnDisplayName() = runBlocking {
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.displayName.collect() }
        viewModel.displayName.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_DISPLAY_NAME)
        }
        collector.cancel()
    }

    @Test
    fun userName_initWithValue_shouldReturnUserName() = runBlocking {
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.username.collect() }
        viewModel.username.test {
            assertThat(awaitItem()).isEqualTo(TEST_USER_NAME)
        }
        collector.cancel()
    }
}
