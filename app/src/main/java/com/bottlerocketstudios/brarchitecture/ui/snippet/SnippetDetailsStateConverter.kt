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
        changeWatchingStatus = ::watchSnippet,
        onCloneClick = ::cloneSnippet,
        onEditClick = ::editSnippet,
        onDeleteClick = ::deleteSnippet,
        onRawClick = ::getRawFile,
        comment = comment.collectAsState(),
        onCommentChanged = { comment.value = it }
    )

