package com.bottlerocketstudios.brarchitecture

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreen
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreenState
import com.bottlerocketstudios.compose.util.asMutableState
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.Test

class PullRequestScreenshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun pullRequestWithListItemsScreenshot() {
        renderComponent(testList)
        compareScreenshot(composeRule.onRoot())
    }

    @Test
    fun pullRequestNoResultItemsScreenshot() {
        renderComponent(emptyList)
        compareScreenshot(composeRule.onRoot())
    }

    fun renderComponent(state: PullRequestScreenState) { composeRule.setContent { PullRequestScreen(state) } }

    // todo move somewhere that both UI test and Compose Preview
    //  can share it instead of setting it twice.
    private val testList = PullRequestScreenState(
        listOf(
            PullRequestItemState(
                prName = "ASAA-19/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "5 days ago".asMutableState(),
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState()
            ),
            PullRequestItemState(
                prName = "ASAA-20/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "4 days ago".asMutableState(),
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState()
            ),
            PullRequestItemState(
                prName = "ASAA-21/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "3 days ago".asMutableState(),
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState()
            ),
            PullRequestItemState(
                prName = "ASAA-22/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "2 days ago".asMutableState(),
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState()
            )
        ).asMutableState(),
        selectedText = "Open".asMutableState(),
        selectionList = listOf("Open", "Merged", "Declined", "Superseded").asMutableState(),
        onFilterSelectionClicked = {}
    )

    private val emptyList = PullRequestScreenState(
        emptyList<PullRequestItemState>().asMutableState(),
        selectedText = "Open".asMutableState(),
        selectionList = listOf("Open", "Merged", "Declined", "Superseded").asMutableState(),
        onFilterSelectionClicked = {}
    )
}
