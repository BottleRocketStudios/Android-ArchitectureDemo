package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
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
    val onRawClick: () -> Unit,
    val comment: State<String>,
    val onCommentChanged: (String) -> Unit
)

@Composable
fun SnippetDetailsScreen(state: SnippetDetailsScreenState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_2,
                    end = Dimens.grid_2,
                )
        ) {
            SnippetTitleLayout(state)
            SnippetDetailsLayout(state)
            EditButtonsLayout(
                isWatching = state.isWatchingSnippet.value,
                onWatchingClick = state.changeWatchingStatus,
                onCloneClick = state.onCloneClick,
                onEditClick = state.onEditClick,
                onDeleteClick = state.onDeleteClick
            )

            FilesLayout(
                files = state.files.value,
                onRawClick = state.onRawClick
            )

            // TODO: Add comments sections?
            // CommentsLayout(
            //     1,
            //     state.currentUser.value,
            //     state.comment.value,
            //     state.onCommentChanged
            // )
        }
    }
}

@Composable
fun SnippetTitleLayout(state: SnippetDetailsScreenState) {
    Row(
        modifier = Modifier
            .padding(top = Dimens.grid_1_5)
    ) {
        AsyncImage(
            model = state.owner.value?.avatarUrl,
            contentDescription = stringResource(R.string.snippet_owner_avatar),
            modifier = Modifier
                .width(Dimens.grid_5)
                .height(Dimens.grid_5)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.ic_avatar_placeholder),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = state.snippetTitle.value,
            style = typography.h1.bold(),
            modifier = Modifier.padding(Dimens.grid_1)
        )
    }
}

@Composable
fun SnippetDetailsLayout(state: SnippetDetailsScreenState) {
    val modifier = Modifier.padding(
        top = Dimens.grid_0_25,
        bottom = Dimens.grid_0_25
    )
    Column {
        val spanStyle = SpanStyle(
            fontSize = typography.h5.fontSize,
            fontWeight = typography.h5.fontWeight,
            fontStyle = typography.h5.fontStyle,
            fontFamily = typography.h5.fontFamily
        )
        Row(modifier = modifier) {
            SnippetDetailsSpan(
                "Owned By ",
                state.owner.value?.displayName ?: "",
                spanStyle,
                spanStyle.copy(color = lightColors.onSurface)
            )
        }

        Row(modifier = modifier) {
            SnippetDetailsSpan(
                "Created By ${state.creator.value?.displayName ?: ""} ",
                state.createdMessage.value,
                spanStyle,
                spanStyle.copy(color = lightColors.onSurface)
            )
        }

        if (state.updatedMessage.value.isNotEmpty()) {
            Row(modifier = modifier) {
                SnippetDetailsSpan(
                    "Last Modified ",
                    state.updatedMessage.value,
                    spanStyle,
                    spanStyle.copy(color = lightColors.onSurface)
                )
            }
        }
    }
}

@Composable
fun EditButtonsLayout(
    isWatching: Boolean,
    onWatchingClick: () -> Unit,
    onCloneClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.grid_2),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SnippetDetailsEditButton(
            icon = Icons.Default.Visibility,
            iconDescription = "Eye Icon on Watch Button",
            buttonText = if (isWatching) "Watching" else "Stop Watching",
            onClick = { onWatchingClick() }
        )
        SnippetDetailsEditButton(buttonText = "Clone", onClick = { onCloneClick() })
        SnippetDetailsEditButton(buttonText = "Edit", onClick = { onEditClick() })
        SnippetDetailsEditButton(buttonText = "Delete", onClick = { onDeleteClick() })
    }
}

@Composable
fun FilesLayout(
    files: List<SnippetDetailsFile?>,
    onRawClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = Dimens.grid_1_5)
    ) {
        Text(
            text = "Snippet Files",
            style = typography.h2.copy(fontWeight = FontWeight.Bold),
        )
        LazyColumn(Modifier.padding(vertical = Dimens.grid_0_5)) {
            items(files) { file ->
                SnippetDetailsFilesCard(file, onRawClick)
            }
        }
    }
}

// TODO: Pass list of comments
@Composable
fun CommentsLayout(
    commentCount: Int?,
    user: User?,
    comment: String,
    onCommentChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = Dimens.grid_1_5)
    ) {
        Text(
            text = if (commentCount == null) "Snippet Comments" else "Snippet Comments ($commentCount)",
            style = typography.h2.copy(fontWeight = FontWeight.Bold),
        )

        Row(
            modifier = Modifier.padding(vertical = Dimens.grid_1_5),
            verticalAlignment = Alignment.CenterVertically
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
                text = comment,
                onChanged = onCommentChanged,
                hint = "What would you like to say?",
                modifier = Modifier.wrapContentHeight()
            )
        }

        // TODO: Lazy Column of comments
        CommentCard(
            userAvatar = "https://i.pinimg.com/736x/69/ed/be/69edbedeccf27136c2ea6b18af6ec49d.jpg",
            userName = "Luke Skywalker",
            userComment = "This is a fake comment on a preivew of a comment card. This represents the comment a user would make.",
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
    spanStyle1: SpanStyle,
    spanStyle2: SpanStyle
) {
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

