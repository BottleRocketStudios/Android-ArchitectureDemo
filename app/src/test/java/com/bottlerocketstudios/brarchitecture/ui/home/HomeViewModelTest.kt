package com.bottlerocketstudios.brarchitecture.ui.home

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.data.converter.convertToGitRepository
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_NAME
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.Clock

class HomeViewModelTest : BaseTest() {
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @Test
    fun user_onVMInit_shouldReturnMatchingUserName() = runTest {
        assertThat(viewModel.user).isNotNull()
        assertThat(viewModel.user.value?.username).isEqualTo(TEST_USER_NAME)
    }

    @Test
    fun repos_onVMInit_shouldReturnMatchingListSize() = runTest {
        assertThat(viewModel.repos.value).hasSize(bitbucketRepository.repos.value.size)
    }

    @Test
    fun userRepositoryState_onVMInit_shouldReturnTestRepo() = runTest {
        MockBitBucketRepo.testGitRepositoryDtoList = listOf(GitRepositoryDto(name = TEST_REPO))
        viewModel.userRepositoryState.test {
            bitbucketRepository.refreshMyRepos()
            assertThat(expectMostRecentItem().first().repo.name).isEqualTo(TEST_REPO)
        }
    }

    @Test
    fun userRepositoryStateIsEmpty_whenSetToEmpty_shouldReturnEmptyList() = runTest {
        MockBitBucketRepo.testGitRepositoryDtoList = emptyList()
        viewModel.userRepositoryState.test {
            bitbucketRepository.refreshMyRepos()
            assertThat(expectMostRecentItem().isEmpty()).isEqualTo(true)
        }
    }

    @Test
    fun itemSelected_emitValue_shouldReturnUserRepoUiModel() = runTest {
        viewModel.itemSelected.test {
            val repo = MockBitBucketRepo.testGitRepositoryDto.convertToGitRepository()
            (viewModel.itemSelected as? MutableSharedFlow)
                ?.emit(UserRepositoryUiModel(repo, repo.updated.formattedUpdateTime(Clock.systemDefaultZone())))
            assertThat(awaitItem().repo.name).isEqualTo(repo.name)
        }
    }

    @Test
    fun itemSelected_whenSelectItemIsCalled_shouldReturnTestRepo() = runTest {
        bitbucketRepository.refreshMyRepos()
        viewModel.itemSelected.test {
            viewModel.selectItem(viewModel.userRepositoryState.first()[0])
            assertThat(awaitItem().repo.name).isEqualTo(TEST_REPO)
        }
    }
}
