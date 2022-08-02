package com.bottlerocketstudios.compose.snippets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.lightColors
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.snippets.snippetDetails.SnippetDetailsScreenState
import com.bottlerocketstudios.compose.snippets.snippetDetails.returnMockSnippetDetails
import com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets.CategoryHeader
import com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets.CommentCard
import com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets.NewCommentInput
import com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets.SnippetDetailsButton
import com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets.SnippetDetailsCloneCard
import com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets.SnippetDetailsFilesCard
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.widgets.CircleAvatarImage
import com.bottlerocketstudios.launchpad.compose.bold

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SnippetDetailsScreen(state: SnippetDetailsScreenState) {
    LazyColumn {
        stickyHeader { SnippetDetailsTitleLayout(state) }
        filesLayout(files = state.files.value)
        commentsLayout(state = state)
    }
}

@Composable
fun SnippetDetailsTitleLayout(state: SnippetDetailsScreenState) {
    Column(
        modifier = Modifier
            .background(Colors.surface)
            .padding(horizontal = Dimens.grid_2)
            .padding(top = Dimens.grid_2_5)
            .wrapContentHeight(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleAvatarImage(
                imgUri = state.snippetDetails.value?.owner?.avatarUrl,
                contentDescription = R.string.description_avatar,
                placeholder = R.drawable.ic_avatar_placeholder,
            )
            Column(
                modifier = Modifier
                    .padding(start = Dimens.grid_2)
                    .align(Alignment.Top)
            ) {
                Text(text = state.snippetDetails.value?.title ?: "", style = typography.h1.bold())
                Column {
                    SnippetDetailsSpan(stringResource(id = R.string.owned_by), state.snippetDetails.value?.owner?.displayName ?: "")
                    SnippetDetailsSpan(
                        stringResource(id = R.string.created_by, state.snippetDetails.value?.creator?.displayName ?: ""),
                        state.snippetDetails.value?.createdMessage ?: ""
                    )
                    if (state.snippetDetails.value?.updatedMessage?.isNotEmpty() == true) {
                        SnippetDetailsSpan(stringResource(id = R.string.last_modified), state.snippetDetails.value?.updatedMessage ?: "")
                    }
                }
            }
        }
        EditButtonsLayout(state)
    }
}

@Composable
fun EditButtonsLayout(state: SnippetDetailsScreenState) {
    var cloneExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(tween(1000))
            .padding(top = Dimens.grid_1)
    ) {
        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            SnippetDetailsButton(
                icon = if (state.isWatchingSnippet.value) Icons.Default.Visibility else Icons.Outlined.Visibility,
                iconDescription = stringResource(id = R.string.description_eye_icon),
                buttonText = stringResource(id = if (state.isWatchingSnippet.value) R.string.stop_watching else R.string.start_watching),
                onClick = state.onSnippetWatchClick
            )
            SnippetDetailsButton(
                buttonText = stringResource(id = R.string.button_clone),
                onClick = { cloneExpanded = !cloneExpanded }
            )
            SnippetDetailsButton(
                buttonText = stringResource(id = R.string.button_edit),
                onClick = state.onSnippetEditClick
            )
            SnippetDetailsButton(
                buttonText = stringResource(id = R.string.button_delete),
                onClick = state.onSnippetDeleteClick
            )
        }

        if (cloneExpanded) {
            Column {
                SnippetDetailsCloneCard(
                    type = stringResource(id = R.string.https),
                    link = state.snippetDetails.value?.httpsCloneLink ?: ""
                )
                SnippetDetailsCloneCard(
                    type = stringResource(id = R.string.ssh),
                    link = state.snippetDetails.value?.sshCloneLink ?: ""
                )
            }
        }
    }
}

fun LazyListScope.filesLayout(files: List<SnippetDetailsFile>) {
    item { CategoryHeader(header = stringResource(id = R.string.header_snippet_files)) }
    items(files) { file ->
        SnippetDetailsFilesCard(file)
    }
}

fun LazyListScope.commentsLayout(state: SnippetDetailsScreenState) {
    item {
        CategoryHeader(
            header = if (state.comments.value.isEmpty())
                stringResource(id = R.string.header_snippet_comments, "")
            else
                stringResource(id = R.string.header_snippet_comments, "(${state.comments.value.size})")
        )
    }

    item {
        NewCommentInput(
            user = state.currentUser.value,
            newComment = state.newSnippetComment.value,
            onCommentChanged = state.onCommentChanged,
            onSaveClicked = state.onSaveCommentClick,
            onCancelClicked = state.onCancelNewCommentClick,
        )
    }
    items(state.comments.value) { comment ->
        CommentCard(
            user = state.currentUser.value,
            replyComment = state.newReplyComment.value,
            onReplyChanged = state.onReplyChanged,
            onCancelClicked = state.onCancelNewCommentClick,
            comment = comment,
            onSaveClick = state.onSaveCommentClick,
            onEditClick = state.onEditCommentClick,
            onDeleteClick = state.onDeleteCommentClick,
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
    Text(
        text = buildAnnotatedString {
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

