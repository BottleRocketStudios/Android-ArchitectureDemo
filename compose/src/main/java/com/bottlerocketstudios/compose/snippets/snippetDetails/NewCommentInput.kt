package com.bottlerocketstudios.compose.snippets.snippetDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.snippets.SnippetDetailsEditButton
import com.bottlerocketstudios.compose.widgets.OutlinedInputField

@Composable
fun NewCommentInput(
    user: User?,
    newComment: String,
    onCommentChanged: (String) -> Unit,
    onSaveClicked: () -> Unit
) {
    var focusState by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = if (focusState) Dimens.grid_0_25 else Dimens.grid_1_5)
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
            hint = if (focusState) "New Comment" else "What would you like to say?",
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = Dimens.grid_2)
                .fillMaxWidth()
                .onFocusChanged { focusState = it.hasFocus }
        )
    }
    if (focusState) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SnippetDetailsEditButton(
                buttonText = "Save",
                onClick = { onSaveClicked() },
                textStyle = typography.body1,
                buttonPadding = Dimens.grid_0_5
            )
            SnippetDetailsEditButton(
                buttonText = "Cancel",
                onClick = { focusManager.clearFocus() },
                textStyle = typography.body1,
                buttonColor = Colors.surface,
                buttonPadding = Dimens.grid_0_5
            )
        }
    }
}
