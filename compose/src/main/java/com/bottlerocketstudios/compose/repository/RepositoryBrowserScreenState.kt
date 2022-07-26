package com.bottlerocketstudios.compose.repository

import androidx.compose.runtime.State

data class RepositoryBrowserScreenState(
    val path: State<String>,
    val itemCount: State<Int>,
    val repositoryItems: State<List<RepositoryItemUiModel>>,
    val onRepositoryItemClicked: (RepositoryItemUiModel) -> Unit
)
