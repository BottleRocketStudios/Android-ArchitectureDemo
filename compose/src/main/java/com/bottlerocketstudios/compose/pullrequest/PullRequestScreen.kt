package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.filterdropdown.PullRequestFilterBy
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.asMutableState

@Composable
fun PullRequestScreen(state: PullRequestScreenState) {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 36.dp, bottom = 24.dp)
                .wrapContentSize()
        ) {
            PullRequestFilterBy(
                state.selectedText.value,
                state.selectionList.value,
                state.onFilterSelectionClicked
            )
        }

        Divider(
            modifier = Modifier.padding(start = 9.dp, end = 9.dp, top = 24.dp, bottom = 24.dp),
            color = brown_grey,
            thickness = 1.dp
        )
        if (state.pullRequestList.value.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = state.pullRequestList.value,
                    itemContent = { item -> PullRequestItemCard(item) }
                )
            }
        } else {
            PullRequestEmptyScreen()
        }
        Divider(
            modifier = Modifier.padding(start = 9.dp, end = 9.dp, top = 24.dp, bottom = 24.dp),
            color = brown_grey,
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
fun PullRequestsPreview() {
    Preview {
        listOf(
            PullRequestScreen(
                PullRequestScreenState(
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
                    selectionList = listOf("Open", "", "").asMutableState(),
                    onFilterSelectionClicked = {}
                )
            )
        )
    }
}
