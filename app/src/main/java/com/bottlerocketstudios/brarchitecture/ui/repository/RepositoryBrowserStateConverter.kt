package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.repository.RepositoryBrowserScreenState

@Composable
fun RepositoryBrowserViewModel.toState() = RepositoryBrowserScreenState(
    path = path.collectAsState(),
    itemCount = itemCount.collectAsState(),
    repositoryItems = uiModels.collectAsState(),
    onRepositoryItemClicked = { item ->
        onRepoItemClicked(item)
    }
)
