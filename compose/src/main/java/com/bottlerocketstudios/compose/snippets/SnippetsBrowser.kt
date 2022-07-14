package com.bottlerocketstudios.compose.snippets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.asMutableState

data class SnippetsBrowserScreenState(
    val snippets: State<List<SnippetUiModel>>,
    val createVisible: State<Boolean>,
    val onCreateSnippetClicked: () -> Unit
)

@Composable
fun SnippetsBrowserScreen(state: SnippetsBrowserScreenState) {
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = state.createVisible.value) {
                SnippetsFabLayout(state.onCreateSnippetClicked)
            }
        }
    ) {
        SnippetsListLayout(snippets = state.snippets.value)
    }
}

@Composable
fun SnippetsListLayout(snippets: List<SnippetUiModel>) {
    if (snippets.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
            modifier = Modifier
                .padding(all = Dimens.grid_1_5)
                .fillMaxSize()
        ) {
            items(
                items = snippets,
                itemContent = { item -> SnippetItem(item) }
            )
        }
    } else {
        SnippetsEmptyLayout()
    }
}

@Composable
private fun SnippetsFabLayout(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onFabClick() },
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_create),
                contentDescription = stringResource(id = R.string.new_snippet)
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun SnippetsBrowserScreenPreview() {
    Preview {
        SnippetsBrowserScreen(
            state = SnippetsBrowserScreenState(
                snippets = listOfMockSnippets.asMutableState(),
                createVisible = true.asMutableState()
            ) {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SnippetsBrowserScreenNoFabPreview() {
    Preview {
        SnippetsBrowserScreen(
            state = SnippetsBrowserScreenState(
                snippets = listOfMockSnippets.asMutableState(),
                createVisible = false.asMutableState()
            ) {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SnippetsBrowserScreenEmptyPreview() {
    Preview {
        SnippetsBrowserScreen(
            state = SnippetsBrowserScreenState(
                snippets = emptyList<SnippetUiModel>().asMutableState(),
                createVisible = true.asMutableState()
            ) {}
        )
    }
}
