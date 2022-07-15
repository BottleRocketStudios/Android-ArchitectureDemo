package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.lightColors
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.resources.very_light_grey
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.launchpad.compose.bold
import com.bottlerocketstudios.launchpad.compose.normal

data class SnippetDetailsScreenState(
    val userAvatar: State<String?>,
    val snippetTitle: State<String>,
    val createdMessage: State<String>,
    val updatedMessage: State<String>,
    val isPrivate: State<Boolean>,
    val files: State<List<SnippetDetailsFile?>>,
    val owner: State<User?>,
    val creator: State<User?>
)

@Composable
fun SnippetDetailsScreen(state: SnippetDetailsScreenState) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = state.snippetTitle.value,
            style = typography.h1.bold(),
            modifier = Modifier
                .padding(
                    start = Dimens.grid_2_5,
                    end = Dimens.grid_1_5,
                    top = Dimens.grid_3_5,
                    bottom = Dimens.grid_1
                )
        )
        ModifiedCard(state.creator.value, state.createdMessage.value, state.updatedMessage.value)
        FilesLazyColumn(files = state.files.value)

    }
}

@Composable
fun ModifiedCard(
    creator: User?,
    createdMessage: String,
    updatedMessage: String
) {
    val modifier = Modifier.padding(
        start = Dimens.grid_2_5,
        end = Dimens.grid_1_5,
        top = Dimens.grid_0_25,
        bottom = Dimens.grid_0_25
    )
    Column {
        Row(modifier = modifier) {
            Text(
                text = "Created by ${creator?.displayName} ",
                style = typography.h5,
            )
            Text(
                text = createdMessage,
                style = typography.h5.copy(color = lightColors.onSurface),
            )
        }

        Row(modifier = modifier) {
            Text(
                text = "Last Modified ",
                style = typography.h5,
            )
            Text(
                text = updatedMessage,
                style = typography.h5.copy(color = lightColors.onSurface),
            )
        }
    }
}

@Composable
fun FilesLazyColumn(files: List<SnippetDetailsFile?>) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.grid_2,
                vertical = Dimens.grid_1
            )
    ) {
        items(files) { file ->
            FilesCard(file)
        }
    }
}

@Composable
fun FilesCard(file: SnippetDetailsFile?) {
    Card(
        shape = MaterialTheme.shapes.small,
        backgroundColor = very_light_grey,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.grid_1)
    ) {
        Column(Modifier.padding(Dimens.grid_2)) {
            Text(
                text = file?.fileName ?: "",
                style = typography.h4.normal()
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun SnippetDetailsScreenPreview() {
    Preview {
        SnippetDetailsScreen(
            returnMockSnippetDetails()
        )
    }
}

