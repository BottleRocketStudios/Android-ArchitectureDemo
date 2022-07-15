package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.snippets.SnippetDetailsScreenState

@Composable
fun SnippetDetailsViewModel.toState() =
    SnippetDetailsScreenState(
        userAvatar = userAvatar.collectAsState(),
        snippetTitle = snippetTitle.collectAsState(),
        createdMessage = createdMessage.collectAsState(),
        updatedMessage = updatedMessage.collectAsState(),
        isPrivate = isPrivate.collectAsState(),
        files = files.collectAsState(),
        owner = owner.collectAsState(),
        creator = creator.collectAsState()
    )

