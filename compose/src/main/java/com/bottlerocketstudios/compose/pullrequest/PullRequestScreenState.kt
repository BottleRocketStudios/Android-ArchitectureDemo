package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.runtime.State

data class PullRequestScreenState(
    val pullRequestList: State<List<PullRequestItemState>>,
    val selectedText: State<String>,
    val selectionList: State<List<String>>,
    val onFilterSelectionClicked: (String) -> Unit,
)

data class PullRequestItemState(
    val prName: State<String>,
    val prState: State<String>,
    val prCreation: State<String>,
    val linesAdded: State<String>,
    val linesRemoved: State<String>,
    val author: State<String>,
    val source: State<String>,
    val destination: State<String>,
    val reviewers: State<String>
)
