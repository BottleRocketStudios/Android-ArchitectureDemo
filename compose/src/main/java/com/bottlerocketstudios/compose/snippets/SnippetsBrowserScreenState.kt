package com.bottlerocketstudios.compose.snippets

import androidx.compose.runtime.State

data class SnippetsBrowserScreenState(
    val snippets: State<List<SnippetUiModel>>,
    val createVisible: State<Boolean>,
    val onCreateSnippetClicked: () -> Unit,
    val onSnippetClick: (SnippetUiModel) -> Unit,
)
