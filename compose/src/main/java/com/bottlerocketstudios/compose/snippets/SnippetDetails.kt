package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.launchpad.compose.bold

data class SnippetDetailsScreenState(
    val snippetDetails: State<SnippetDetailsUiModel?>
)

@Composable
fun SnippetDetailsScreen(state: SnippetDetailsScreenState) {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = state.snippetDetails.value?.title ?: "",
                style = MaterialTheme.typography.h1.bold(),
                modifier = Modifier
                    .padding(
                        start = Dimens.grid_2_5,
                        end = Dimens.grid_1_5,
                        top = Dimens.grid_3_5,
                        bottom = Dimens.grid_1_5
                    )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SnippetDetailsScreenPreview() {
    Preview {
        SnippetDetailsScreen(
            SnippetDetailsScreenState(
                mockSnippetDetails.asMutableState()
            )
        )
    }
}

