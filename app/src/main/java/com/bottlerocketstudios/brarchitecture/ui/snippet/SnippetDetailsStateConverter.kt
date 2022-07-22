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
        httpsLink = httpsCloneLink.collectAsState(),
        sshLink = sshCloneLink.collectAsState(),
        copyHttps = ::copyHttps,
        copySsh = ::copySsh,
        isWatchingSnippet = isWatchingSnippet.collectAsState(),
        changeWatchingStatus = ::changeSnippetWatching,
        onSnippetEditClick = ::editSnippet,
        onSnippetDeleteClick = ::deleteSnippet,
        comments = snippetComments.collectAsState(),
        newSnippetComment = newSnippetComment.collectAsState(),
        newReplyComment = newReplyComment.collectAsState(),
        onCommentChanged = { newSnippetComment.value = it },
        onReplyChanged = { newReplyComment.value = it },
        onSaveCommentClick = ::saveComment,
        onCancelNewCommentClick = ::cancelCommentCreation,
        onEditCommentClick = ::commentEditClick,
        onDeleteCommentClick = ::commentDeleteClick,
    )

