package com.bottlerocketstudios.compose.home

import androidx.compose.runtime.State

data class HomeScreenState(
    val pullRequests: State<List<UserPullRequestUIModel>>,
    val repositories: State<List<UserRepositoryUiModel>>,
    val repositorySelected: (userRepositoryUiModel: UserRepositoryUiModel) -> Unit,
)
