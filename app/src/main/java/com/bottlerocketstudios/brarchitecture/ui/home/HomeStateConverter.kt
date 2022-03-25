package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.home.HomeScreenState

@Composable
fun HomeViewModel.toState() = HomeScreenState(
    repositories = userRepositoryState.collectAsState(emptyList())
)
