package com.bottlerocketstudios.compose.snippets.snippetDetails

import androidx.compose.runtime.State
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User

data class SnippetDetailsScreenState(
    val snippetDetails: State<SnippetDetailsUiModel?>,
    val currentUser: State<User?>,
    val files: State<List<SnippetDetailsFile>>,
    val isWatchingSnippet: State<Boolean>,
    val onSnippetWatchClick: () -> Unit,
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
