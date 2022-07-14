package com.bottlerocketstudios.brarchitecture.ui.repository

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_HASH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_PATH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO
import com.bottlerocketstudios.compose.repository.RepositoryItemUiModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RepositoryBrowserViewModelTest : BaseTest() {
    private lateinit var viewModel: RepositoryBrowserViewModel

    @Before
    fun setUp() {
        runTest {
            bitbucketRepository.refreshUser()
            bitbucketRepository.refreshMyRepos()
        }
        inlineKoinSingle { bitbucketRepository }
        viewModel = RepositoryBrowserViewModel()
    }

    @Test
    fun srcFiles_getFilesCalled_shouldSetSrcFiles() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.srcFiles.test {
            assertThat(awaitItem()[0].path).isEqualTo(viewModel.path.value)
        }
    }

    @Test
    fun srcFiles_initialState_shouldReturnEmptyList() = runTest {
        viewModel.srcFiles.test {
            assertThat(awaitItem()).isEqualTo(emptyList<RepoFile>())
        }
    }

    @Test
    fun currentRepoName_getFilesCalled_shouldReturnTestRepo() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        assertThat(viewModel.currentRepoName).isEqualTo(TEST_REPO)
    }

    @Test
    fun currentRepoName_initialState_shouldBeBlank() = runTest {
        assertThat(viewModel.currentRepoName).isEmpty()
    }

    @Test
    fun path_getFilesCalled_shouldReturnTestPath() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.path.test {
            assertThat(awaitItem()).isEqualTo(TEST_PATH)
        }
    }

    @Test
    fun path_folderPathIsNull_shouldReturnRepoName() = runTest {
        viewModel.path.test {
            viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, null))
            awaitItem() // Skip init value
            assertThat(awaitItem()).isEqualTo(TEST_REPO)
        }
    }

    @Test
    fun itemCount_initialValue_shouldReturnZero() = runTest {
        assertThat(viewModel.itemCount.value).isEqualTo(0)
    }

    @Test
    fun itemCount_onInitialization_shouldReturnSizeOfFilesList() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.itemCount.test {
            assertThat(awaitItem()).isEqualTo(viewModel.srcFiles.value.size)
        }
    }

    @Test
    fun uiModels_srcFilesNotEmpty_shouldNotReturnEmptyList() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.uiModels.test {
            assertThat(awaitItem().size).isEqualTo(viewModel.srcFiles.value.size)
        }
    }

    @Test
    fun uiModels_srcFilesEmpty_shouldReturnEmptyList() = runTest {
        assertThat(viewModel.uiModels.value).isEmpty()
    }

    @Test
    fun uiModels_srcFilesNotEmpty_testModelData() = runTest {
        // Multiple asserts to test contents of list item; if one fails the entire test should fail
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.uiModels.test {
            val firstItem = awaitItem().first()
            assertThat(firstItem.path).isEqualTo(TEST_PATH)
            assertThat(firstItem.size).isEqualTo(0)
            assertThat(firstItem.isFolder).isEqualTo(true)
        }
    }

    @Test
    fun onRepoItemClicked_itemIsFolder_directoryClickEventValueMatches() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.directoryClickedEvent.test {
            viewModel.onRepoItemClicked(RepositoryItemUiModel(TEST_PATH, 0, true))
            assertThat(awaitItem().repoName).isEqualTo(viewModel.currentRepoName)
        }
    }

    @Test
    fun onRepoItemClicked_itemIsFile_fileClickEventMatches() = runTest {
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))

        viewModel.fileClickedEvent.test {
            viewModel.onRepoItemClicked(RepositoryItemUiModel(TEST_PATH, 0, false))

            assertThat(awaitItem().path).isEqualTo(TEST_PATH)
        }
    }
}
