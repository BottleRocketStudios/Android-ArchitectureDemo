package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.snippets.SnippetDetailsScreenState

@Composable
fun SnippetDetailsViewModel.toState() =
    SnippetDetailsScreenState(
        currentUser = currentUser.collectAsState(),
        snippetTitle = snippetTitle.collectAsState(),
        createdMessage = createdMessage.collectAsState(),
        updatedMessage = updatedMessage.collectAsState(),
        isPrivate = isPrivate.collectAsState(),
        files = files.collectAsState(),
        owner = owner.collectAsState(),
        creator = creator.collectAsState(),
        isWatchingSnippet = isWatchingSnippet.collectAsState(),
        changeWatchingStatus = ::changeSnippetWatching,
        onCloneClick = ::cloneSnippet,
        onEditClick = ::editSnippet,
        onDeleteClick = ::deleteSnippet,
        comments = snippetComments.collectAsState(),
        newSnippetComment = newSnippetComment.collectAsState(),
        onCommentChanged = { newSnippetComment.value = it }
    )

