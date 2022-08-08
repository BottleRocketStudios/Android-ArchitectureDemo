package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.runtime.State
import com.bottlerocketstudios.compose.filterdropdown.FilterDropDownState

data class PullRequestScreenState(
    val pullRequestList: State<List<PullRequestItemState>>,
    val filterDropDownState: FilterDropDownState,
)

data class PullRequestItemState(
    val prName: State<String>,
    val prState: State<String>,
    val prCreation: State<String>,
    val linesAdded: State<String>,
    val linesRemoved: State<String>
)
