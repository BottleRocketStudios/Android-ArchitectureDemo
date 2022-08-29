package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.snippets.snippetDetails.SnippetDetailsScreenState

@Composable
fun SnippetDetailsViewModel.toState() =
    SnippetDetailsScreenState(
        snippetDetails = snippetDetails.collectAsState(),
        currentUser = currentUser.collectAsState(null),
        files = snippetFiles.collectAsState(),
        isWatchingSnippet = isWatchingSnippet.collectAsState(),
        onSnippetWatchClick = ::changeSnippetWatching,
        onSnippetEditClick = ::onEditSnippetClick,
        onSnippetDeleteClick = ::onDeleteSnippetClick,
        comments = snippetComments.collectAsState(),
        newSnippetComment = newSnippetComment.collectAsState(),
        newReplyComment = newReplyComment.collectAsState(),
        onCommentChanged = { newSnippetComment.value = it },
        onReplyChanged = { newReplyComment.value = it },
        onSaveCommentClick = ::onCommentSaveEvent,
        onCancelNewCommentClick = ::clearCommentValues,
        onEditCommentClick = ::commentEditClick,
        onDeleteCommentClick = ::commentDeleteClick,
    )
