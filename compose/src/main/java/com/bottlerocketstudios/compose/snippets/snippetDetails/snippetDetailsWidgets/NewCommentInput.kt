package com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.snippets.snippetDetails.returnMockSnippetDetails
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.widgets.CircleAvatarImage
import com.bottlerocketstudios.compose.widgets.OutlinedInputField

@Composable
fun NewCommentInput(
    user: User?,
    parentId: Int? = null,
    newComment: String,
    onCommentChanged: (String) -> Unit,
    onSaveClicked: ((Int?) -> Unit),
    onCancelClicked: () -> Unit,
) {
    var focusState by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .animateContentSize(tween(1000))
            .padding(horizontal = Dimens.grid_2)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = if (focusState) Dimens.grid_0_25 else Dimens.grid_1_5)
        ) {
            CircleAvatarImage(imgUri = user?.avatarUrl, sizeDp = Dimens.grid_5)

            OutlinedInputField(
                text = newComment,
                onChanged = onCommentChanged,
                hint = stringResource(id = if (focusState) R.string.new_snippet_comment else R.string.snippet_comment_hint),
                modifier = Modifier
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
                SnippetDetailsPrimaryButton(
                    buttonText = stringResource(id = R.string.button_save),
                    onClick = {
                        onSaveClicked(parentId)
                        focusManager.clearFocus()
                    },
                    textStyle = typography.body1,
                    buttonPadding = Dimens.grid_0_5
                )
                SnippetDetailsPrimaryButton(
                    buttonText = stringResource(id = R.string.button_cancel),
                    onClick = {
                        onCancelClicked()
                        focusManager.clearFocus()
                    },
                    textStyle = typography.body1,
                    buttonColor = Colors.surface,
                    buttonPadding = Dimens.grid_0_5
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommentInput() {
    val mockData = returnMockSnippetDetails()
    Preview {
        NewCommentInput(
            user = mockData.currentUser.value,
            parentId = null,
            newComment = mockData.newSnippetComment.value,
            onCommentChanged = {},
            onSaveClicked = {},
            onCancelClicked = {},
            )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommentInputWithContent() {
    val mockData = returnMockSnippetDetails()
    Preview {
        NewCommentInput(
            user = mockData.currentUser.value,
            parentId = null,
            newComment = "Fake Comment",
            onCommentChanged = {},
            onSaveClicked = {},
            onCancelClicked = {},
        )
    }
}
