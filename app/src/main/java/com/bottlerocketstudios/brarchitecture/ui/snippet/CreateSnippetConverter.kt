package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreenState

@Composable
fun CreateSnippetViewModel.toState() = CreateSnippetScreenState(
    title = title.collectAsState(),
    filename = filename.collectAsState(),
    contents = contents.collectAsState(),
    isPrivate = private.collectAsState(),
    creationFailed = failed.collectAsState(),
    createEnabled = createEnabled.collectAsState(),
    onTitleChanged = { title.value = it },
    onFilenameChanged = { filename.value = it },
    onContentsChanged = { contents.value = it },
    onPrivateChanged = { private.value = it },
    onCreateClicked = ::onCreateClick,
)
