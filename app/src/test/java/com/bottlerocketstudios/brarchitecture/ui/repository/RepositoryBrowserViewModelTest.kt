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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        viewModel.getFiles(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH))
    }

    @Test
    fun srcFiles_getFilesCalledOnInit_shouldSetSrcFiles() = runBlocking {
        viewModel.srcFiles.test {
            assertThat(awaitItem()[0].path).isEqualTo(viewModel.path.value)
        }
    }

    @Test
    fun srcFiles_setValue_shouldReturnEmptyList() = runBlocking {
        viewModel.srcFiles.test {
            // Init value set by getFiles
            assertThat(awaitItem()).isNotEmpty()
            // Set value to empty list
            viewModel.srcFiles.value = emptyList()
            // Assert value is empty list
            assertThat(awaitItem()).isEqualTo(emptyList<RepoFile>())
        }
    }

    @Test
    fun currentRepoName_getFilesCalledOnInit_shouldReturnTestRepo() = runBlocking {
        assertThat(viewModel.currentRepoName).isEqualTo(TEST_REPO)
    }

    @Test
    fun currentRepoName_setToEmptyString_shouldBeBlank() = runBlocking {
        viewModel.currentRepoName = ""
        assertThat(viewModel.currentRepoName).isEmpty()
    }

    @Test
    fun path_getFilesCalledOnInit_shouldReturnTestPath() = runBlocking {
        viewModel.path.test {
            assertThat(awaitItem()).isEqualTo(TEST_PATH)
        }
    }

    @Test
    fun itemCount_initialValue_shouldReturnZero() = runBlocking {
        assertThat(viewModel.itemCount.value).isEqualTo(0)
    }

    @Test
    fun itemCount_onInitialization_shouldReturnSizeOfFilesList() = runBlocking {
        // Must start collection() for any flow using the .groundState extension
        // https://developer.android.com/kotlin/flow/test#statein
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.itemCount.collect() }

        assertThat(viewModel.itemCount.value).isEqualTo(viewModel.srcFiles.value.size)

        collector.cancel()
    }

    @Test
    fun uiModels_srcFilesNotEmpty_shouldNotReturnEmptyList() = runBlocking {
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.uiModels.collect() }

        assertThat(viewModel.uiModels.value.size).isEqualTo(viewModel.srcFiles.value.size)

        collector.cancel()
    }

    @Test
    fun uiModels_srcFilesEmpty_shouldReturnEmptyList() = runBlocking {
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.uiModels.collect() }
        viewModel.srcFiles.value = emptyList()

        assertThat(viewModel.uiModels.value.isEmpty()).isEqualTo(true)

        collector.cancel()
    }

    @Test
    fun uiModels_srcFilesNotEmpty_testModelData() = runBlocking {
        // Multiple asserts to test contents of list item; if one fails the entire test should fail
        val collector = launch(testDispatcherProvider.Unconfined) { viewModel.uiModels.collect() }

        assertThat(viewModel.uiModels.value.first().path).isEqualTo(TEST_PATH)
        assertThat(viewModel.uiModels.value.first().size).isEqualTo(0)
        assertThat(viewModel.uiModels.value.first().isFolder).isEqualTo(true)

        collector.cancel()
    }

    @Test
    fun onRepoItemClicked_itemIsFolder_directoryClickEventValueMatches() = runBlocking {
        viewModel.directoryClickedEvent.test {
            viewModel.onRepoItemClicked(RepositoryItemUiModel(TEST_PATH, 0, true))
            assertThat(awaitItem().repoName).isEqualTo(viewModel.currentRepoName)
        }
    }

    @Test
    fun onRepoItemClicked_itemIsFile_fileClickEventMatches() = runBlocking {
        viewModel.fileClickedEvent.test {
            viewModel.onRepoItemClicked(RepositoryItemUiModel(TEST_PATH, 0, false))

            assertThat(awaitItem().path).isEqualTo(TEST_PATH)
        }
    }
}
