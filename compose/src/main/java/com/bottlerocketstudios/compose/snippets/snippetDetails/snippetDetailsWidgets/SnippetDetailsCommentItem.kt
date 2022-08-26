package com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.ONE_SECOND_MILLIS
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.snippets.snippetDetails.returnMockSnippetDetails
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.widgets.CircleAvatarImage
import com.bottlerocketstudios.launchpad.compose.light

@Suppress("LongMethod", "LongParameterList")
@Composable
fun CommentCard(
    user: User?,
    replyComment: String,
    onReplyChanged: (String) -> Unit,
    comment: SnippetComment,
    onSaveClick: (Int?) -> Unit,
    onCancelClicked: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier
            .animateContentSize(tween(ONE_SECOND_MILLIS))
            .padding(horizontal = Dimens.grid_2)
    ) {
        Row(
            modifier = Modifier.padding(vertical = Dimens.grid_1_5)
        ) {
            CircleAvatarImage(
                imgUri = user?.avatarUrl,
                sizeDp = Dimens.grid_4,
                contentDescription = R.string.description_avatar,
                placeholder = R.drawable.ic_avatar_placeholder,
            )

            Column(
                modifier = Modifier.padding(horizontal = Dimens.grid_1_5)
            ) {
                Text(
                    text = comment.user?.displayName ?: "",
                    style = typography.h4.copy(color = Colors.onSurface),
                )
                Text(
                    text = comment.content?.raw ?: "",
                    style = typography.h5.light(),
                    modifier = Modifier.padding(top = Dimens.grid_0_5)
                )
                Row(
                    modifier = Modifier.padding(top = Dimens.grid_0_5)
                ) {
                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.button_reply)),
                        style = typography.body1.copy(color = Colors.tertiary),
                        onClick = { expanded = true }
                    )
                    Text(text = "  •  ")
                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.button_edit)),
                        style = typography.body1.copy(color = Colors.tertiary),
                        onClick = { comment.id?.let { it1 -> onEditClick(it1) } }
                    )
                    Text(text = "  \u2022  ")
                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.button_delete)),
                        style = typography.body1.copy(color = Colors.tertiary),
                        onClick = { comment.id?.let { it1 -> onDeleteClick(it1) } }
                    )
                    Text(text = "  \u2022  ")
                    Text(
                        text = comment.updated ?: comment.created ?: "",
                        style = typography.body2.light().copy(color = Colors.onSurface),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                comment.childrenComments.forEach { comment ->
                    comment.let {
                        CommentCard(
                            user = user,
                            replyComment = replyComment,
                            onReplyChanged = onReplyChanged,
                            onCancelClicked = onCancelClicked,
                            comment = comment,
                            onSaveClick = onSaveClick,
                            onEditClick = { comment.id?.let { it1 -> onEditClick(it1) } },
                            onDeleteClick = { comment.id?.let { it1 -> onDeleteClick(it1) } },
                        )
                    }
                }
            }
        }
        if (expanded) {
            NewCommentInput(
                user = user,
                parentId = comment.id,
                newComment = replyComment,
                onCommentChanged = onReplyChanged,
                onSaveClicked = {
                    expanded = false
                    onSaveClick(comment.id)
                },
                onCancelClicked = {
                    expanded = false
                    onCancelClicked()
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCommentCard() {
    val mockData = returnMockSnippetDetails()
    Preview {
        CommentCard(
            user = mockData.currentUser.value,
            replyComment = mockData.newReplyComment.value,
            onReplyChanged = {},
            comment = mockData.comments.value[0],
            onCancelClicked = {},
            onSaveClick = { Unit },
            onEditClick = { Unit },
            onDeleteClick = { Unit },
        )
    }
}
