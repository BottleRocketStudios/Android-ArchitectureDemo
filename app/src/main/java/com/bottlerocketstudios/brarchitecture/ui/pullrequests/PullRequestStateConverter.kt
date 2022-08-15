package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreenState

@Composable
fun PullRequestViewModel.toState() = PullRequestScreenState(
    pullRequestList = pullRequestList.collectAsState(initial = emptyList()),
    selectedText = selectedText.collectAsState(),
    selectionList = selectionList.collectAsState(initial = emptyList()),
    onFilterSelectionClicked = { selectedText.value = it }
)
