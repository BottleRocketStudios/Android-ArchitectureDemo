package com.bottlerocketstudios.compose.projects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.compose.util.asMutableState

@Composable
fun ProjectsScreen(state: ProjectsScreenState) {
    Column {
        if (state.projectsList.value.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
                modifier = Modifier.fillMaxSize()
                    .padding(top = Dimens.grid_2)
            ) {
                items(
                    items = state.projectsList.value,
                    itemContent = { item -> ProjectsItemCard(item) }
                )
            }
        } else {
            ProjectsEmptyScreen()
        }
    }
}

@PreviewComposable
@Composable
private fun PullRequestsPreview() {
    Preview {
        listOf(
            ProjectsScreen(
                ProjectsScreenState(
                    listOf(
                        ProjectsItemState(
                            name = "Projecto".asMutableState(),
                            key = "Keyto".asMutableState(),
                            updated = "77 Days Ago".asMutableState()
                        ),
                        ProjectsItemState(
                            name = "Projecta".asMutableState(),
                            key = "Keyto".asMutableState(),
                            updated = "88 Days Ago".asMutableState()
                        ),
                        ProjectsItemState(
                            name = "Projecttoni".asMutableState(),
                            key = "Keyto".asMutableState(),
                            updated = "99 Days Ago".asMutableState()
                        ),
                    ).asMutableState(),
                )
            )
        )
    }
}
