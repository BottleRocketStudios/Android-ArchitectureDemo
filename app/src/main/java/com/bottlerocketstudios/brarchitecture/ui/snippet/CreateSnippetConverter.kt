package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreenState

@Composable
fun CreateSnippetFragmentViewModel.toState() = CreateSnippetScreenState(
    title = title.collectAsState(),
    filename = filename.collectAsState(),
    contents = contents.collectAsState(),
    private = private.collectAsState(),
    failed = failed.collectAsState(),
    createEnabled = createEnabled.collectAsState(),
    onCreateClicked = ::onCreateClick
)
