package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreenState

@Composable
fun PullRequestViewModel.toState() = PullRequestScreenState(
    pullRequestList = this.pullRequestRequestList.collectAsState(initial = emptyList()),
    selectedText = this.selectedText.collectAsState(),
    selectionList = this.selectionList.collectAsState(initial = emptyList()),
    onFilterSelectionClicked = { selectedText.value = it }
)
