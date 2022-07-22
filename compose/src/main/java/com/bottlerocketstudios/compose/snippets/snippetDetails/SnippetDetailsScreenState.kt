package com.bottlerocketstudios.compose.snippets.snippetDetails

import androidx.compose.runtime.State
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User

data class SnippetDetailsScreenState(
    val currentUser: State<User?>,
    val snippetTitle: State<String>,
    val createdMessage: State<String>,
    val updatedMessage: State<String>,
    val isPrivate: State<Boolean>,
    val files: State<List<SnippetDetailsFile?>>,
    val owner: State<User?>,
    val creator: State<User?>,
    val httpsLink: State<String>,
    val sshLink: State<String>,
    val copyHttps: () -> Unit, // TODO: remove unnecessary
    val copySsh: () -> Unit, // TODO: remove unnecessary
    val isWatchingSnippet: State<Boolean>,
    val changeWatchingStatus: () -> Unit,
    val onSnippetEditClick: () -> Unit,
    val onSnippetDeleteClick: () -> Unit,
    val comments: State<List<SnippetComment>>,
    val newSnippetComment: State<String>,
    val newReplyComment: State<String>,
    val onCommentChanged: (String) -> Unit,
    val onReplyChanged: (String) -> Unit,
    val onSaveCommentClick: (Int?) -> Unit,
    val onCancelNewCommentClick: () -> Unit,
    val onEditCommentClick: (Int) -> Unit,
    val onDeleteCommentClick: (Int) -> Unit
)
