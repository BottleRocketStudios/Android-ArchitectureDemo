package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.home.HomeScreenState

@Composable
fun HomeViewModel.toState() = HomeScreenState(
    pullRequests = userPullRequestState.collectAsState(emptyList()),
    repositories = userRepositoryState.collectAsState(emptyList()),
    itemSelected = ::selectItem
)
