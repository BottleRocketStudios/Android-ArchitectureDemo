package com.bottlerocketstudios.compose.snippets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.lightColors
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.snippets.snippetDetails.CategoryHeader
import com.bottlerocketstudios.compose.snippets.snippetDetails.NewCommentInput
import com.bottlerocketstudios.compose.snippets.snippetDetails.SnippetDetailsScreenState
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.launchpad.compose.bold

@Composable
fun SnippetDetailsScreen(state: SnippetDetailsScreenState) {
    LazyColumn(modifier = Modifier.padding(horizontal = Dimens.grid_2)) {
        item { SnippetTitleLayout(state) }
        // item { SnippetDetailsLayout(state) }
        item {
            EditButtonsLayout(
                isWatching = state.isWatchingSnippet.value,
                httpsLink = state.httpsLink.value,
                sshLink = state.sshLink.value,
                onCopyHttps = state.copyHttps,
                onCopySsh = state.copySsh,
                onWatchingClick = state.changeWatchingStatus,
                onEditClick = state.onSnippetEditClick,
                onDeleteClick = state.onSnippetDeleteClick
            )
        }

        // TODO: Move category headers to respective "layouts"
        item { CategoryHeader(header = stringResource(id = R.string.header_snippet_files)) }

        filesLayout(files = state.files.value)

        item {
            CategoryHeader(
                header = if (state.comments.value.isEmpty())
                    stringResource(id = R.string.header_snippet_comments, "")
                else
                    stringResource(id = R.string.header_snippet_comments, "(${state.comments.value.size})")
            )
        }

        // TODO: move to comments layout
        item {
            NewCommentInput(
                user = state.currentUser.value,
                newComment = state.newSnippetComment.value,
                onCommentChanged = state.onCommentChanged,
                onSaveClicked = state.onSaveCommentClick,
                onCancelClicked = state.onCancelNewCommentClick,
            )
        }

        commentsLayout(
            user = state.currentUser.value,
            replyComment = state.newReplyComment.value,
            onReplyChanged = state.onReplyChanged,
            onCancelClicked = state.onCancelNewCommentClick,
            comments = state.comments.value,
            onSaveClick = state.onSaveCommentClick,
            onEditCommentClick = state.onEditCommentClick,
            onDeleteCommentClick = state.onDeleteCommentClick,
        )
    }
}

@Composable
fun SnippetTitleLayout(state: SnippetDetailsScreenState) {
    Row(
        modifier = Modifier
            .padding(top = Dimens.grid_3)
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = state.owner.value?.avatarUrl,
            contentDescription = stringResource(R.string.snippet_owner_avatar),
            modifier = Modifier
                .width(Dimens.grid_7)
                .height(Dimens.grid_7)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.ic_avatar_placeholder),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .padding(start = Dimens.grid_2)
                .align(Alignment.Top)
        ) {
            Text(text = state.snippetTitle.value, style = typography.h1.bold())
            SnippetDetailsLayout(state)
        }

    }
}

@Composable
fun SnippetDetailsLayout(state: SnippetDetailsScreenState) {
    Column {
        SnippetDetailsSpan(stringResource(id = R.string.owned_by), state.owner.value?.displayName ?: "")
        SnippetDetailsSpan(
            stringResource(id = R.string.created_by, state.creator.value?.displayName ?: ""),
            state.createdMessage.value
        )
        if (state.updatedMessage.value.isNotEmpty()) {
            SnippetDetailsSpan(stringResource(id = R.string.last_modified), state.updatedMessage.value)
        }
    }
}

//TODO: Pass state to layout and values to widgets
@Composable
fun EditButtonsLayout(
    isWatching: Boolean,
    httpsLink: String?,
    onCopyHttps: () -> Unit,
    sshLink: String?,
    onCopySsh: () -> Unit,
    onWatchingClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var cloneExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.animateContentSize(tween(1000))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.grid_3),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SnippetDetailsEditButton(
                icon = if (isWatching) Icons.Default.Visibility else Icons.Outlined.Visibility,
                iconDescription = stringResource(id = R.string.eye_icon_description),
                buttonText =
                if (isWatching) stringResource(id = R.string.stop_watching) else stringResource(id = R.string.start_watching),
                onClick = { onWatchingClick() })
            SnippetDetailsEditButton(
                buttonText = stringResource(id = R.string.button_clone),
                onClick = { cloneExpanded = !cloneExpanded }
            )
            SnippetDetailsEditButton(
                buttonText = stringResource(id = R.string.button_edit),
                onClick = { onEditClick() }
            )
            SnippetDetailsEditButton(
                buttonText = stringResource(id = R.string.button_delete),
                onClick = { onDeleteClick() }
            )
        }

        if (cloneExpanded) {
            Column {
                SnippetDetailsCloneCard(
                    type = stringResource(id = R.string.https),
                    link = httpsLink ?: "",
                    copyClick = onCopyHttps
                )
                SnippetDetailsCloneCard(
                    type = stringResource(id = R.string.ssh),
                    link = sshLink ?: "",
                    copyClick = onCopySsh
                )
            }
        }
    }
}

fun LazyListScope.filesLayout(
    files: List<SnippetDetailsFile?>
) {
    items(files) { file ->
        SnippetDetailsFilesCard(file)
    }
}

fun LazyListScope.commentsLayout(
    user: User?,
    replyComment: String,
    onReplyChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    comments: List<SnippetComment>,
    onDeleteCommentClick: (Int) -> Unit,
    onEditCommentClick: (Int) -> Unit,
    onSaveClick: (Int?) -> Unit,
) {
    items(comments) { comment ->
        CommentCard(
            user = user,
            replyComment = replyComment,
            onReplyChanged = onReplyChanged,
            onCancelClicked = onCancelClicked,
            comment = comment,
            onSaveClick = onSaveClick,
            onEditClick = onEditCommentClick,
            onDeleteClick = onDeleteCommentClick,
        )
    }
}

@Composable
fun SnippetDetailsSpan(
    text: String,
    appendedText: String,
) {
    val spanStyle1 = SpanStyle(
        fontSize = typography.h5.fontSize,
        fontWeight = typography.h5.fontWeight,
        fontStyle = typography.h5.fontStyle,
        fontFamily = typography.h5.fontFamily
    )
    val spanStyle2 = spanStyle1.copy(color = lightColors.onSurface)
    Text(text = buildAnnotatedString {
        withStyle(style = spanStyle1) { append(text) }
        withStyle(spanStyle2) { append(" $appendedText") }
    }
    )
}

@Preview(showSystemUi = true)
@Composable
fun SnippetDetailsScreenPreview() {
    Preview {
        SnippetDetailsScreen(
            returnMockSnippetDetails()
        )
    }
}

