package com.bottlerocketstudios.compose.snippets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class SnippetDetailsScreenState(
    val snippetDetails: State<SnippetDetailsUiModel?>
)

@Composable
fun SnippetDetails(state: SnippetDetailsScreenState) {
}
