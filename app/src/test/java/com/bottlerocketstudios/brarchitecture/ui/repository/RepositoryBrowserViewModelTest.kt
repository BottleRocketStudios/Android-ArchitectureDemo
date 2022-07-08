package com.bottlerocketstudios.brarchitecture.ui.repository

import app.cash.turbine.test
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
    private val unconfinedDispatcher = testDispatcherProvider.Unconfined

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
    fun repositoryBrowserViewModel_getFiles_shouldSetCurrentName() = runBlocking {
        assertThat(viewModel.currentRepoName).isEqualTo(TEST_REPO)
    }

    @Test
    fun repositoryBrowserViewModel_getFiles_shouldSetPathFlow() = runBlocking {
        viewModel.path.test {
            assertThat(awaitItem()).isEqualTo(TEST_PATH)
        }
    }

    @Test
    fun repositoryBrowserViewModel_getFiles_shouldSetSrcFiles() = runBlocking {
        viewModel.srcFiles.test {
            assertThat(awaitItem().first().path).isEqualTo(viewModel.path.value)
        }
    }

    @Test
    fun repositoryBrowserViewModel_itemCount_shouldReturnInitialValue() = runBlocking {
        assertThat(viewModel.itemCount.value).isEqualTo(0)
    }

    @Test
    fun repositoryBrowserViewModel_itemCount_shouldReturnSize() = runBlocking {
        // Must start collection() for any flow using the .groundState extension
        // https://developer.android.com/kotlin/flow/test#statein
        val collector = launch(unconfinedDispatcher) { viewModel.itemCount.collect() }
        assertThat(viewModel.itemCount.value).isEqualTo(viewModel.srcFiles.value.size)
        collector.cancel()
    }

    @Test
    fun repositoryBrowserViewModel_uiModels_shouldNotReturnEmptyList() = runBlocking {
        val collector = launch(unconfinedDispatcher) { viewModel.uiModels.collect() }
        assertThat(viewModel.uiModels.value.size).isEqualTo(viewModel.srcFiles.value.size)
        collector.cancel()
    }

    @Test
    fun repositoryBrowserViewModel_uiModels_shouldReturnEmptyList() = runBlocking {
        val collector = launch(unconfinedDispatcher) { viewModel.uiModels.collect() }
        viewModel.srcFiles.value = emptyList()
        assertThat(viewModel.uiModels.value.isEmpty()).isEqualTo(true)
        collector.cancel()
    }

    @Test
    fun repositoryBrowserViewModel_uiModels_testModelData() = runBlocking {
        // Multiple asserts in one test -- testing the contents of the list item.
        val collector = launch(unconfinedDispatcher) { viewModel.uiModels.collect() }
        assertThat(viewModel.uiModels.value.first().path).isEqualTo(TEST_PATH)
        assertThat(viewModel.uiModels.value.first().size).isEqualTo(0)
        assertThat(viewModel.uiModels.value.first().isFolder).isEqualTo(true)

        collector.cancel()
    }

    @Test
    fun repositoryBrowserViewModel_onRepoItemClicked_folderEmissionMatches() = runBlocking {
        viewModel.directoryClickedEvent.test {
            viewModel.onRepoItemClicked(RepositoryItemUiModel(TEST_PATH, 0, true))
            assertThat(awaitItem().repoName).isEqualTo(viewModel.currentRepoName)
        }
    }

    @Test
    fun repositoryBrowserViewModel_onRepoItemClicked_fileEmissionMatches() = runTest {
        viewModel.fileClickedEvent.test {
            viewModel.onRepoItemClicked(RepositoryItemUiModel(TEST_PATH, 0, false))
            assertThat(awaitItem().path).isEqualTo(TEST_PATH)
        }
    }
}
