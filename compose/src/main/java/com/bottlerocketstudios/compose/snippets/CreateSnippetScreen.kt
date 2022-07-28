package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.widgets.OutlinedInputField
import com.bottlerocketstudios.launchpad.compose.bold
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun CreateSnippetScreen(state: CreateSnippetScreenState) {
    val uiState by remember { mutableStateOf(state) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedInputField(
            text = uiState.title.value,
            onChanged = uiState.onTitleChanged,
            hint = stringResource(id = R.string.create_snippet_title_hint),
            modifier = Modifier
                .padding(top = Dimens.grid_2, bottom = Dimens.grid_1, start = Dimens.grid_7, end = Dimens.grid_7)
                .fillMaxWidth()
        )
        OutlinedInputField(
            text = uiState.filename.value,
            onChanged = uiState.onFilenameChanged,
            hint = stringResource(id = R.string.create_snippet_filename_hint),
            modifier = Modifier
                .padding(top = Dimens.grid_1, bottom = Dimens.grid_1, start = Dimens.grid_7, end = Dimens.grid_7)
                .fillMaxWidth()
        )
        OutlinedInputField(
            text = uiState.contents.value,
            onChanged = uiState.onContentsChanged,
            hint = stringResource(id = R.string.create_snippet_contents_hint),
            maxLines = 5,
            imeAction = ImeAction.Done,
            modifier = Modifier
                .padding(top = Dimens.grid_1, bottom = Dimens.grid_1, start = Dimens.grid_7, end = Dimens.grid_7)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp)
        )
        LabelledCheckbox(isPrivate = uiState.isPrivate.value, onCheckedChange = uiState.onPrivateChanged)
        FailedText(creationFailed = uiState.creationFailed.value)
        CreateSnippetButton(createEnabled = uiState.createEnabled.value, onCreateClicked = uiState.onCreateClicked)
    }
}

@Composable
fun CreateSnippetButton(createEnabled: Boolean, onCreateClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TextButton(
            onClick = { onCreateClicked() },
            enabled = createEnabled,
            colors = ButtonDefaults.textButtonColors(backgroundColor = ArchitectureDemoTheme.colors.primary),
            modifier = Modifier
                .padding(top = Dimens.grid_0_5)
                .defaultMinSize(minWidth = Dimens.grid_12_5)
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
fun FailedText(creationFailed: Boolean) {
    val text = if (creationFailed) {
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
fun LabelledCheckbox(isPrivate: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = Dimens.grid_5,
                end = Dimens.grid_5,
            )
    ) {
        Checkbox(
            checked = isPrivate,
            onCheckedChange = { onCheckedChange(it) },
            enabled = true,
            colors = CheckboxDefaults.colors(ArchitectureDemoTheme.colors.tertiary)
        )
        Text(text = stringResource(id = R.string.create_snippet_private))
    }
}

@PreviewAll
@Composable
fun CreateSnippetScreenPreview() {
    Preview {
        CreateSnippetScreen(
            state = previewState()
        )
    }
}

@Composable
fun previewState() = CreateSnippetScreenState(
    title = "".asMutableState(),
    filename = "".asMutableState(),
    contents = "".asMutableState(),
    isPrivate = true.asMutableState(),
    creationFailed = false.asMutableState(),
    createEnabled = true.asMutableState(),
    onCreateClicked = {},
    onTitleChanged = {},
    onFilenameChanged = {},
    onContentsChanged = {},
    onPrivateChanged = {}
)
