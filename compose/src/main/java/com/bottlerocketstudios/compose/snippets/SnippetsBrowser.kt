package com.bottlerocketstudios.compose.snippets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class SnippetsBrowserScreenState(
    val snippets: State<List<SnippetUiModel>>,
    val onCreateSnippetClicked: () -> Unit
)

@Composable
fun SnippetsBrowserScreen(state: SnippetsBrowserScreenState) {
}
