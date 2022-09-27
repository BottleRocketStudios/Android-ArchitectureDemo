package com.bottlerocketstudios.compose.home

import androidx.compose.runtime.State
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState

data class HomeScreenState(
    val pullRequests: State<List<PullRequestItemState>>,
    val repositories: State<List<UserRepositoryUiModel>>,
    val itemSelected: (userRepositoryUiModel: UserRepositoryUiModel) -> Unit,
)
