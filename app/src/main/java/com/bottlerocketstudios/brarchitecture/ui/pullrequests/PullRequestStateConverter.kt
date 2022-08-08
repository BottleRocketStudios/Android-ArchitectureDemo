package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.filterdropdown.FilterDropDownState
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreenState

@Composable
fun PullRequestViewModel.toState() = PullRequestScreenState(
    pullRequestList = this.pullRequestRequestList.collectAsState(initial = emptyList()),
    filterDropDownState = FilterDropDownState(
        selectionList = listOf("Open", "Closed", "Merged"),
        onFilterSelectionClicked = { this.onFilterByStateClicked(it) }
    )
)
