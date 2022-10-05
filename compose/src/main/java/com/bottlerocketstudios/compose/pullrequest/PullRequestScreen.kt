package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.filterdropdown.FilterDropDownMenu
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.compose.util.asMutableState

@Composable
fun PullRequestScreen(state: PullRequestScreenState) {
    Column {
        FilterDropDownMenu(
            Modifier.padding(start = Dimens.grid_2, top = Dimens.grid_4_5, bottom = Dimens.grid_3),
            state.selectedText.value,
            state.selectionList.value,
            state.onFilterSelectionClicked
        )

        Divider(
            modifier = Modifier.padding(start = Dimens.grid_1, end = Dimens.grid_1, bottom = Dimens.grid_3),
            color = Colors.borderColor,
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
            modifier = Modifier.padding(start = Dimens.grid_1, end = Dimens.grid_1, top = Dimens.grid_3, bottom = Dimens.grid_3),
            color = Colors.borderColor,
            thickness = 1.dp
        )
    }
}

@PreviewComposable
@Composable
private fun PullRequestsPreview() {
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
                            linesRemoved = "0 Lines Removed".asMutableState(),
                            author = "Arthur Morgan".asMutableState(),
                            source = "Branch".asMutableState(),
                            destination = "master".asMutableState(),
                            reviewers = "No Reviewers".asMutableState(),
                        ),
                        PullRequestItemState(
                            prName = "ASAA-20/PR-Screen".asMutableState(),
                            prState = "Open".asMutableState(),
                            prCreation = "4 days ago".asMutableState(),
                            linesAdded = "0 Lines Added".asMutableState(),
                            linesRemoved = "0 Lines Removed".asMutableState(),
                            author = "Jeff Robbman".asMutableState(),
                            source = "Branch".asMutableState(),
                            destination = "master".asMutableState(),
                            reviewers = "No Reviewers".asMutableState(),
                        ),
                        PullRequestItemState(
                            prName = "ASAA-21/PR-Screen".asMutableState(),
                            prState = "Open".asMutableState(),
                            prCreation = "3 days ago".asMutableState(),
                            linesAdded = "0 Lines Added".asMutableState(),
                            linesRemoved = "0 Lines Removed".asMutableState(),
                            author = "Ash Ketchem".asMutableState(),
                            source = "Branch".asMutableState(),
                            destination = "master".asMutableState(),
                            reviewers = "No Reviewers".asMutableState(),
                        ),
                        PullRequestItemState(
                            prName = "ASAA-22/PR-Screen".asMutableState(),
                            prState = "Open".asMutableState(),
                            prCreation = "2 days ago".asMutableState(),
                            linesAdded = "0 Lines Added".asMutableState(),
                            linesRemoved = "0 Lines Removed".asMutableState(),
                            author = "Sans Undertale".asMutableState(),
                            source = "Branch".asMutableState(),
                            destination = "master".asMutableState(),
                            reviewers = "No Reviewers".asMutableState(),
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
