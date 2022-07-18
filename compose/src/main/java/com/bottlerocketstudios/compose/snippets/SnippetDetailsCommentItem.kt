package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.launchpad.compose.light

// TODO: Replace arguments with Comment Data Class
@Composable
fun CommentCard(
    userAvatar: String,
    userName: String,
    userComment: String,
    onReplyClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(modifier = Modifier.padding(vertical = Dimens.grid_1_5)) {
        AsyncImage(
            modifier = Modifier
                .width(Dimens.grid_5)
                .height(Dimens.grid_5)
                .clip(CircleShape),
            model = userAvatar,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_avatar_placeholder),
            contentDescription = "User Avatar"
        )

        Column(modifier = Modifier.padding(horizontal = Dimens.grid_1_5)) {
            Text(
                text = userName,
                style = typography.h4.copy(color = Colors.onSurface),
            )
            Text(
                text = userComment,
                style = typography.h5.light(),
                modifier = Modifier.padding(top = Dimens.grid_1_5)
            )
            Row(
                modifier = Modifier.padding(top = Dimens.grid_1)
            ) {
                ClickableText(
                    text = AnnotatedString("Reply"),
                    style = typography.body1.copy(color = Colors.tertiary),
                    onClick = { onReplyClick() }
                )
                Text(text = "  •  ")
                ClickableText(
                    text = AnnotatedString("Edit"),
                    style = typography.body1.copy(color = Colors.tertiary),
                    onClick = { onEditClick() }
                )
                Text(text = "  \u2022  ")
                ClickableText(
                    text = AnnotatedString("Delete"),
                    style = typography.body1.copy(color = Colors.tertiary),
                    onClick = { onDeleteClick() }
                )
                // TODO: Add text when comment was created
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommentCard() {
    Preview {
        CommentCard(
            userAvatar = "https://i.pinimg.com/736x/69/ed/be/69edbedeccf27136c2ea6b18af6ec49d.jpg",
            userName = "Luke Skywalker",
            userComment = "This is a fake comment on a preivew of a comment card. This represents the comment a user would make.",
            onReplyClick = { Unit },
            onEditClick  = { Unit },
            onDeleteClick  = { Unit }
        )
    }
}
