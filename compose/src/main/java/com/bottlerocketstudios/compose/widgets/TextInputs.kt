package com.bottlerocketstudios.compose.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun OutlinedInputField(
    text: String,
    onChanged: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    maxLines: Int = 1,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = { onChanged(it) },
        label = { Text(text = hint) },
        textStyle = TextStyle(color = ArchitectureDemoTheme.colors.onBackground),
        maxLines = maxLines,
        singleLine = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun OutlinedInputFieldPreview() {
    Preview {
        OutlinedInputField(
            text = "",
            onChanged = {},
            hint = "UserName",
            modifier = Modifier
                .padding(
                    start = Dimens.grid_7,
                    end = Dimens.grid_7
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun OutlinedInputFieldPreFilledPreview() {
    Preview {
        OutlinedInputField(
            text = "BobRoss@gmail.com",
            onChanged = {},
            hint = "UserName",
            modifier = Modifier
                .padding(
                    start = Dimens.grid_7,
                    end = Dimens.grid_7
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
    }
}
