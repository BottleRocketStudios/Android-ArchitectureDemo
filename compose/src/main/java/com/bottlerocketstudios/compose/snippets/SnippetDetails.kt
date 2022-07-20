package com.bottlerocketstudios.compose.snippets

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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.widgets.OutlinedInputField
import com.bottlerocketstudios.launchpad.compose.bold

data class SnippetDetailsScreenState(
    val currentUser: State<User?>,
    val snippetTitle: State<String>,
    val createdMessage: State<String>,
    val updatedMessage: State<String>,
    val isPrivate: State<Boolean>,
    val files: State<List<SnippetDetailsFile?>>,
    val owner: State<User?>,
    val creator: State<User?>,
    val isWatchingSnippet: State<Boolean>,
    val changeWatchingStatus: () -> Unit,
    val onCloneClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onDeleteClick: () -> Unit,
    val comments: State<List<SnippetComment>>,
    val newSnippetComment: State<String>,
    val onCommentChanged: (String) -> Unit
)

@Composable
fun SnippetDetailsScreen(state: SnippetDetailsScreenState) {
    LazyColumn(modifier = Modifier.padding(horizontal = Dimens.grid_2)) {
        item { SnippetTitleLayout(state) }
        // item { SnippetDetailsLayout(state) }
        item {
            EditButtonsLayout(
                isWatching = state.isWatchingSnippet.value,
                onWatchingClick = state.changeWatchingStatus,
                onCloneClick = state.onCloneClick,
                onEditClick = state.onEditClick,
                onDeleteClick = state.onDeleteClick
            )
        }

        item { CategoryHeader(header = "Snippet Files") }

        FilesLayout(files = state.files.value)

        item {
            CategoryHeader(
                header = if (state.comments.value.isEmpty())
                    "Snippet Comments" else "Snippet Comments (${state.comments.value.size})"
            )
        }

        item {
            NewCommentInput(
                user = state.currentUser.value,
                newComment = state.newSnippetComment.value,
                onCommentChanged = state.onCommentChanged
            )
        }

        CommentsLayout(comments = state.comments.value)
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
        SnippetDetailsSpan("Owned By ", state.owner.value?.displayName ?: "")
        SnippetDetailsSpan("Created By ${state.creator.value?.displayName ?: ""} ", state.createdMessage.value)
        if (state.updatedMessage.value.isNotEmpty() && state.updatedMessage.value != state.createdMessage.value) {
            SnippetDetailsSpan("Last Modified ", state.updatedMessage.value)
        }
    }
}

@Composable
fun EditButtonsLayout(
    isWatching: Boolean,
    onWatchingClick: () -> Unit,
    onCloneClick: () -> Unit, onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = Dimens.grid_3),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SnippetDetailsEditButton(
            icon = if (isWatching) Icons.Default.Visibility else Icons.Outlined.Visibility,
            iconDescription = "Eye Icon on Watch Button",
            buttonText = if (isWatching) "Stop Watching" else "Start Watching",
            onClick = { onWatchingClick() })
        SnippetDetailsEditButton(buttonText = "Clone", onClick = { onCloneClick() })
        SnippetDetailsEditButton(buttonText = "Edit", onClick = { onEditClick() })
        SnippetDetailsEditButton(buttonText = "Delete", onClick = { onDeleteClick() })
    }
}

fun LazyListScope.FilesLayout(
    files: List<SnippetDetailsFile?>
) {
    items(files) { file ->
        SnippetDetailsFilesCard(file)
    }
}

@Composable
fun CategoryHeader(header: String) {
    Text(
        text = header,
        style = typography.h2.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = Dimens.grid_1_5, bottom = Dimens.grid_0_25)
    )
}

@Composable
fun NewCommentInput(
    user: User?,
    newComment: String,
    onCommentChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = Dimens.grid_1_5)
    ) {
        AsyncImage(
            modifier = Modifier
                .width(Dimens.grid_5)
                .height(Dimens.grid_5)
                .clip(CircleShape),
            model = user?.avatarUrl,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_avatar_placeholder),
            contentDescription = "User Avatar"
        )
        OutlinedInputField(
            text = newComment,
            onChanged = onCommentChanged,
            hint = if (newComment.isEmpty()) "What would you like to say?" else "New Comment",
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = Dimens.grid_2)
                .fillMaxWidth()
        )
    }
}

fun LazyListScope.CommentsLayout(comments: List<SnippetComment>) {
    items(comments) { comment ->
        CommentCard(
            comment = comment,
            onReplyClick = { Unit },
            onEditClick = { Unit },
            onDeleteClick = { Unit }
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
        withStyle(spanStyle2) { append(appendedText) }
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

