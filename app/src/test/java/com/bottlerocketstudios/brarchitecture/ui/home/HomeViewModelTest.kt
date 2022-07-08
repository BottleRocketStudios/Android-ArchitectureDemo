package com.bottlerocketstudios.brarchitecture.ui.home

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_NAME
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class HomeViewModelTest : BaseTest() {

    private lateinit var viewModel: HomeViewModel
    private val repo = MockBitBucketRepo

    @Before
    fun setUp() {
        inlineKoinSingle { bitbucketRepository }
        viewModel = HomeViewModel()
    }

    @Test
    fun homeViewModel_shouldHaveUser_whenInitialized() = runBlocking {
        assertThat(viewModel.user).isNotNull()
        assertThat(viewModel.user.value?.username).isEqualTo(TEST_USER_NAME)
    }

    @Test
    fun homeViewModel_shouldHaveRepos_whenReposRefreshed() = runBlocking {
        assertThat(viewModel.repos.value).hasSize(bitbucketRepository.repos.value.size)
    }

    @Test
    fun homeViewModel_userRepositoryState_shouldReturnTestRepo() = runBlocking {
        repo.testGitRepositoryDtoList = listOf(GitRepositoryDto(name = TEST_REPO))
        viewModel.userRepositoryState.test {
            bitbucketRepository.refreshMyRepos()
            assertThat(expectMostRecentItem().first().repo.name).isEqualTo(TEST_REPO)
        }
    }

    @Test
    fun homeViewModel_userRepositoryStateIsEmpty_whenReposIsEmpty() = runBlocking {
        repo.testGitRepositoryDtoList = emptyList()
         viewModel.userRepositoryState.test {
             bitbucketRepository.refreshMyRepos()
             assertThat(expectMostRecentItem().isEmpty()).isEqualTo(true)
         }
    }

    @Test
    fun homeViewModel_itemSelectedShouldHaveValue_whenSelectItemIsCalled() = runBlocking {
        bitbucketRepository.refreshMyRepos()
        viewModel.itemSelected.test {
            viewModel.selectItem(viewModel.userRepositoryState.first()[0])
            assertThat(awaitItem().repo.name).isEqualTo(TEST_REPO)
        }
    }
}
