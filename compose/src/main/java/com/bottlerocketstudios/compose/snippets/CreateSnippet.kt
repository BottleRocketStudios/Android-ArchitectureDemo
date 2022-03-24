package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
    val onCreateClicked: () -> Unit,
)

@Composable
fun CreateSnippetScreen(state: CreateSnippetScreenState) {
    val uiState by remember { mutableStateOf(state) }

    Column() {
        TextInputBox(text = uiState.title.value, stringResource(id = R.string.create_snippet_title_hint), 1, topPadding = Dimens.grid_2)
        TextInputBox(text = uiState.filename.value, stringResource(id = R.string.create_snippet_filename_hint), 1)
        TextInputBox(text = uiState.contents.value, stringResource(id = R.string.create_snippet_contents_hint), 4)
        LabelledCheckbox(enabled = uiState.private.value)
        FailedText(failed = uiState.failed.value)
        CreateSnippetButton { uiState.onCreateClicked() }
    }
}

@Composable
fun CreateSnippetButton(onCreateClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TextButton(
            onClick = { onCreateClicked() },
            colors = ButtonDefaults.textButtonColors(backgroundColor = ArchitectureDemoTheme.colors.primary),
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
fun FailedText(failed: Boolean) {
    val text = if (failed) {
        stringResource(id = R.string.snippet_creation_failed)
    } else {
        ""
    }
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style =  MaterialTheme.typography.h4.normal(),
        modifier = Modifier
            .padding(
                start = Dimens.grid_7,
                end = Dimens.grid_7,
            )
    )
}

@Composable
fun LabelledCheckbox(enabled: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = Dimens.grid_5,
                end = Dimens.grid_5,
            )
    ) {
        val isChecked = remember { mutableStateOf(enabled) }

        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it },
            enabled = true,
            colors = CheckboxDefaults.colors(ArchitectureDemoTheme.colors.tertiary)
        )
        Text(text = stringResource(id = R.string.create_snippet_private))
    }
}

@Composable
fun TextInputBox(text: String, hint: String, numOfLines: Int, topPadding: Dp = Dimens.grid_1, bottomPadding: Dp = Dimens.grid_1) {

    var value by remember { mutableStateOf(text) }

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = {
            Text(
                text = hint
            )
        },
        textStyle = TextStyle(
            color = ArchitectureDemoTheme.colors.onBackground
        ),
        maxLines = numOfLines,
        modifier = Modifier
            .padding(
                start = Dimens.grid_7,
                end = Dimens.grid_7,
                top = topPadding,
                bottom = bottomPadding
            )
            .fillMaxWidth()
            .wrapContentHeight()
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
                onCreateClicked = {}
            )
        )
    }
}
