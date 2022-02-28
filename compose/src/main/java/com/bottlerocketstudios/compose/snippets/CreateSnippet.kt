package com.bottlerocketstudios.compose.snippets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class CreateSnippetScreenState(
    val title: State<String>,
    val fileName: State<String>,
    val contents: State<String>,
    val private: State<Boolean>,
    val failed: State<Boolean>,
    val createEnabled: State<Boolean>,
    val onTitleChanged: (String) -> Unit,
    val onFilenameChanged: (String) -> Unit,
    val onContentsChanged: (String) -> Unit,
    val onPrivateChanged: (Boolean) -> Unit,
    val onCreateClicked: () -> Unit,
)

@Composable
fun CreateSnippetScreen(state: CreateSnippetScreenState) {
}
