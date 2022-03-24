package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.bold
import com.bottlerocketstudios.compose.resources.normal
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.asMutableState

data class CreateSnippetScreenState(
    val title: State<String>,
    val filename: State<String>,
    val contents: State<String>,
    val private: State<Boolean>,
    val failed: State<Boolean>,
    val createEnabled: State<Boolean>,
    val onTitleChanged: (String) -> Unit,
    val onFilenameChanged: (String) -> Unit,
    val onContentsChanged: (String) -> Unit,
    val onPrivateChanged: (Boolean) -> Unit,
    val onCreateClicked: () -> Unit,
)

@Composable
fun CreateSnippetScreen(state: CreateSnippetScreenState) {
    val uiState by remember { mutableStateOf(state) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextInputBox(
            text = uiState.title.value,
            onChanged = uiState.onTitleChanged,
            hint = stringResource(id = R.string.create_snippet_title_hint),
            topPadding = Dimens.grid_2
        )
        TextInputBox(
            text = uiState.filename.value,
            onChanged = uiState.onFilenameChanged,
            hint = stringResource(id = R.string.create_snippet_filename_hint),
        )
        TextInputBox(
            text = uiState.contents.value,
            onChanged = uiState.onContentsChanged,
            hint = stringResource(id = R.string.create_snippet_contents_hint),
            minLines = 5,
            imeAction = ImeAction.Done
        )
        LabelledCheckbox(uiState = uiState)
        FailedText(uiState = uiState)
        CreateSnippetButton(uiState = uiState)
    }
}

@Composable
fun CreateSnippetButton(uiState: CreateSnippetScreenState) {
    val enabled = uiState.createEnabled.value
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TextButton(
            onClick = { uiState.onCreateClicked() },
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = ArchitectureDemoTheme.colors.primary
            ),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5
                )
                .defaultMinSize(
                    minWidth = Dimens.grid_12_5
                )
        ) {
            Text(
                stringResource(id = R.string.create_snippet_button).uppercase(),
                color = ArchitectureDemoTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3.bold()
            )
        }
    }
}

@Composable
fun FailedText(uiState: CreateSnippetScreenState) {
    val text = if (uiState.failed.value) {
        stringResource(id = R.string.snippet_creation_failed)
    } else {
        ""
    }
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4.normal(),
        modifier = Modifier
            .padding(
                start = Dimens.grid_7,
                end = Dimens.grid_7,
                top = Dimens.grid_1,
                bottom = Dimens.grid_1
            )
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

@Composable
fun LabelledCheckbox(uiState: CreateSnippetScreenState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = Dimens.grid_5,
                end = Dimens.grid_5,
            )
    ) {
        Checkbox(
            checked = uiState.private.value,
            onCheckedChange = { uiState.onPrivateChanged(it) },
            enabled = true,
            colors = CheckboxDefaults.colors(ArchitectureDemoTheme.colors.tertiary)
        )
        Text(text = stringResource(id = R.string.create_snippet_private))
    }
}

@Composable
fun TextInputBox(
    text: String,
    onChanged: (String) -> Unit,
    hint: String,
    minLines: Int = 1,
    topPadding: Dp = Dimens.grid_1,
    bottomPadding: Dp = Dimens.grid_1,
    imeAction: ImeAction = ImeAction.Next
) {
    val focusManager = LocalFocusManager.current
    val lineHeight = MaterialTheme.typography.h2.fontSize * 4/3

    OutlinedTextField(
        value = text,
        onValueChange = { onChanged(it) },
        label = {
            Text(
                text = hint
            )
        },
        textStyle = TextStyle(
            color = ArchitectureDemoTheme.colors.onBackground
        ),
        maxLines = minLines,
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
        modifier = Modifier
            .padding(
                start = Dimens.grid_7,
                end = Dimens.grid_7,
                top = topPadding,
                bottom = bottomPadding
            )
            .fillMaxWidth()
            .sizeIn(minHeight = with(LocalDensity.current) {
                (lineHeight * minLines).toDp()
            })
    )
}

@Preview(showSystemUi = true)
@Composable
fun CreateSnippetScreenPreview() {
    Preview {
        CreateSnippetScreen(
            state = CreateSnippetScreenState(
                title = "".asMutableState(),
                filename = "".asMutableState(),
                contents = "".asMutableState(),
                private = true.asMutableState(),
                failed = false.asMutableState(),
                createEnabled = true.asMutableState(),
                onCreateClicked = {},
                onTitleChanged = {},
                onFilenameChanged = {},
                onContentsChanged = {},
                onPrivateChanged = {}
            )
        )
    }
}
