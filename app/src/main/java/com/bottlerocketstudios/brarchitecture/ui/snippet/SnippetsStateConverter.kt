package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreenState

@Composable
fun SnippetsFragmentViewModel.toState() = SnippetsBrowserScreenState(
    snippets = snippets.collectAsState(emptyList()),
    onCreateSnippetClicked = ::onCreateClick
)
