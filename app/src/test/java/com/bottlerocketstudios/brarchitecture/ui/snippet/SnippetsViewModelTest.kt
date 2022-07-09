package com.bottlerocketstudios.brarchitecture.ui.snippet

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo.bitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.SNIPPET_TITLE
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_USER_DISPLAY_NAME
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.Clock

class SnippetsViewModelTest : BaseTest() {
    private lateinit var viewModel: SnippetsViewModel

    @Before
    fun setUp() {
        inlineKoinSingle { bitbucketRepository }
        viewModel = SnippetsViewModel()
    }

    @Test
    fun snippets_refreshSnippets_shouldReturnSnippetListWithCorrectContent() = runBlocking {
        viewModel.snippets.test {
            viewModel.refreshSnippets()
            // Drop the initial "emptyList()" value and get the updated value from refresh call
            awaitItem().drop(1)

            // Collect the next flow value
            val snippetList = awaitItem()

            // Multiple Asserts checking list size and content; All should match or entire test should fail
            assertThat(snippetList.size).isEqualTo(1)
            assertThat(snippetList.first().title).isEqualTo(SNIPPET_TITLE)
            assertThat(snippetList.first().userName).isEqualTo(TEST_USER_DISPLAY_NAME)
            assertThat(snippetList.first().formattedLastUpdatedTime)
                .isEqualTo(
                    MockBitBucketRepo.snippetDto.updated.formattedUpdateTime(clock = Clock.systemDefaultZone())
                )
        }
    }

    @Test
    fun createClicked_onCreateClicked_shouldEmitUnit() = runBlocking {
        viewModel.createClicked.test {
            viewModel.onCreateClick()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }
}
