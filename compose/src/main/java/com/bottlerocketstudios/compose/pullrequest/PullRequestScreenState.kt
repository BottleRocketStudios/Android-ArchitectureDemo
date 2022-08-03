package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.runtime.State

data class PullRequestScreenState(
    val pullRequestList: State<List<PullRequestItemState>>,
)

data class PullRequestItemState(
    val prName: State<String>,
    val prState: State<String>,
    val prCreation: State<String>,
    val linesAdded: State<Int>,
    val linesRemoved: State<Int>
)
