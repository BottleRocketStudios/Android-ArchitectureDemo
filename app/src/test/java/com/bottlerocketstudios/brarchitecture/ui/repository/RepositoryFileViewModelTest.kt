package com.bottlerocketstudios.brarchitecture.ui.repository

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_HASH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_PATH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO_ID
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO_MIME
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryFileViewModelTest : BaseTest() {
    private lateinit var viewModel: RepositoryFileViewModel

    @Before
    fun setUp() {
        inlineKoinSingle { bitbucketRepository }
        viewModel = RepositoryFileViewModel()
        viewModel.loadFile("", TEST_REPO_ID, TEST_REPO_MIME, TEST_HASH, TEST_PATH)
    }

    @Test
    fun srcFile_onInitialization_shouldByteArray() = runBlocking {
        viewModel.srcFile.test {
            assertThat(awaitItem()?.size).isEqualTo(4)
        }
    }

    @Test
    fun path_onInitialization_shouldReturnPath() = runBlocking {
        viewModel.path.test {
            assertThat(awaitItem()).isEqualTo(TEST_PATH)
        }
    }
}
