package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.repository.RepositoryCommitScreenState

@Composable
fun RepositoryCommitViewModel.toState() = RepositoryCommitScreenState(
    path = path.collectAsState(),
    itemCount = itemCount.collectAsState(),
    commitItems = uiModels.collectAsState()
    // onRepositoryItemClicked = { item ->
    //     onRepoItemClicked(item)
    // }
)
