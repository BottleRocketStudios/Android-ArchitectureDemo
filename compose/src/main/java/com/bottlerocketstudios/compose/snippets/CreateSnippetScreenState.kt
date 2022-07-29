package com.bottlerocketstudios.compose.snippets

import androidx.compose.runtime.State

data class CreateSnippetScreenState(
    val title: State<String>,
    val filename: State<String>,
    val contents: State<String>,
    val isPrivate: State<Boolean>,
    val creationFailed: State<Boolean>,
    val createEnabled: State<Boolean>,
    val onTitleChanged: (String) -> Unit,
    val onFilenameChanged: (String) -> Unit,
    val onContentsChanged: (String) -> Unit,
    val onPrivateChanged: (Boolean) -> Unit,
    val onCreateClicked: () -> Unit,
)
