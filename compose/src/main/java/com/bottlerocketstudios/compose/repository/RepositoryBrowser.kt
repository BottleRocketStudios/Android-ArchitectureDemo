package com.bottlerocketstudios.compose.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

// TODO update code so this can be used for both top level repo and folder views
data class RepositoryBrowserScreenState(
    val path: State<String>,
    val itemCount: State<Int>,
    val repositoryItems: State<List<RepositoryItemUiModel>>,
    val onRepositoryItemClicked: (RepositoryItemUiModel) -> Unit
)

@Composable
fun RepositoryBrowserScreen(state: RepositoryBrowserScreenState) {
}
