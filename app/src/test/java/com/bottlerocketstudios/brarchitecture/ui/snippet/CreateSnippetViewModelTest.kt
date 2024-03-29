package com.bottlerocketstudios.brarchitecture.ui.snippet

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.SNIPPET_CONTENTS
import com.bottlerocketstudios.brarchitecture.test.mocks.SNIPPET_FILENAME
import com.bottlerocketstudios.brarchitecture.test.mocks.SNIPPET_TITLE
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateSnippetViewModelTest : BaseTest() {
    private lateinit var viewModel: CreateSnippetViewModel

    @Before
    fun setUp() {
        inlineKoinSingle { bitbucketRepository }
        viewModel = CreateSnippetViewModel()
    }

    @Test
    fun title_setWithTitle_shouldReturnTestTitle() = runTest {
        viewModel.title.test {
            // Assert initial value is an empty string
            assertThat(awaitItem().isBlank()).isEqualTo(true)
            viewModel.title.value = SNIPPET_TITLE
            assertThat(awaitItem()).isEqualTo(SNIPPET_TITLE)
        }
    }

    @Test
    fun filename_setWithFileName_shouldReturnTestFilename() = runTest {
        viewModel.filename.test {
            // Assert initial value is an empty string
            assertThat(awaitItem().isBlank()).isEqualTo(true)
            viewModel.filename.value = SNIPPET_FILENAME
            assertThat(awaitItem()).isEqualTo(SNIPPET_FILENAME)
        }
    }

    @Test
    fun contents_setWithContents_shouldReturnTestContents() = runTest {
        viewModel.contents.test {
            // Assert initial value is an empty string
            assertThat(awaitItem().isBlank()).isEqualTo(true)
            viewModel.contents.value = SNIPPET_CONTENTS
            assertThat(awaitItem()).isEqualTo(SNIPPET_CONTENTS)
        }
    }

    @Test
    fun private_setToTrue_shouldReturnTrue() = runTest {
        viewModel.private.test {
            // Assert initial value is false
            assertThat(awaitItem()).isEqualTo(false)
            viewModel.private.value = true
            assertThat(awaitItem()).isEqualTo(true)
        }
    }

    @Test
    fun failed_setToTrue_shouldReturnTrue() = runTest {
        viewModel.failed.test {
            // Assert initial value is false
            assertThat(awaitItem()).isEqualTo(false)
            (viewModel.failed as? MutableStateFlow<Boolean>)?.value = true
            assertThat(awaitItem()).isEqualTo(true)
        }
    }

    @Test
    fun failed_onCreateClickedWithSuccess_shouldReturnFalse() = runTest {
        viewModel.failed.test {
            // Set values
            setSnippetValuesAndPrivacy(false)
            viewModel.onCreateClick()
            assertThat(awaitItem()).isEqualTo(false)
        }
    }

    @Test
    fun createEnabled_addValues_shouldReturnTrue() = runTest {
        viewModel.createEnabled.test {
            // Assert initial value is false
            assertThat(awaitItem()).isEqualTo(false)
            // Set values
            setSnippetValuesAndPrivacy(false)
            assertThat(awaitItem()).isEqualTo(true)
        }
    }

    @Test
    fun onSuccess_onCreateClickedSuccess_shouldEmitUnit() = runTest {
        setSnippetValuesAndPrivacy(true)

        viewModel.onSuccess.test {
            viewModel.onCreateClick()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun failed_onCreateClickedFailure_shouldReturnTrue() = runTest {
        setSnippetValuesAndPrivacy(true)
        MockBitBucketRepo.causeFailure = true

        viewModel.failed.test {
            viewModel.onCreateClick()
            // onCreateClick first sets failed to false
            assertThat(awaitItem()).isEqualTo(false)
            // Then Status.Failure setting failed to true
            assertThat(awaitItem()).isEqualTo(true)
        }
    }

    private fun setSnippetValuesAndPrivacy(isPrivate: Boolean) {
        viewModel.title.value = SNIPPET_TITLE
        viewModel.filename.value = SNIPPET_FILENAME
        viewModel.contents.value = SNIPPET_CONTENTS
        MockBitBucketRepo.isPrivate = isPrivate
    }
}
