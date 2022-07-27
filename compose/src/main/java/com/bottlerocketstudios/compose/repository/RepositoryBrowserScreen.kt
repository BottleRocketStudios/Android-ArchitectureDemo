package com.bottlerocketstudios.compose.repository

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.compose.util.pluralResource
import com.bottlerocketstudios.compose.widgets.IconText

@Composable
fun RepositoryBrowserScreen(state: RepositoryBrowserScreenState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HeaderItem(
                path = state.path.value,
                itemCount = state.itemCount.value
            )
        }
        items(items = state.repositoryItems.value) {
            RepositoryItem(
                item = it,
                onItemClicked = { item ->
                    state.onRepositoryItemClicked(item)
                }
            )
        }
    }
}

@Composable
fun HeaderItem(path: String, itemCount: Int) {
    Surface {
        Column(
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1,
                    start = Dimens.grid_1,
                    end = Dimens.grid_1
                )
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = Dimens.grid_1)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconText(
                    iconRes = R.drawable.ic_folders,
                    iconColor = Colors.tertiary,
                    text = path,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = pluralResource(
                        id = R.plurals.folder_items_plural,
                        quantity = itemCount,
                        formatArgs = arrayOf(itemCount)
                    ),
                    style = MaterialTheme.typography.h5
                )
            }
            Divider(
                modifier = Modifier.padding(top = Dimens.grid_1_5)
            )
        }
    }
}

@Composable
fun RepositoryItem(item: RepositoryItemUiModel, onItemClicked: (RepositoryItemUiModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.grid_1,
                vertical = Dimens.grid_0_5
            )
            .clickable {
                onItemClicked(item)
            },
        elevation = Dimens.plane_3
    ) {
        Row {
            val iconRes = if (item.isFolder) {
                R.drawable.ic_folders
            } else {
                R.drawable.ic_file
            }
            Icon(
                modifier = Modifier
                    .padding(
                        start = Dimens.grid_2_5,
                        top = Dimens.grid_2_5
                    ),
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Colors.tertiary
            )
            Column(
                modifier = Modifier.padding(
                    start = Dimens.grid_2_5,
                    top = Dimens.grid_2
                )
            ) {
                Text(
                    text = item.path,
                    style = MaterialTheme.typography.h3.copy(color = Colors.tertiary)
                )
                Text(
                    modifier = Modifier.padding(
                        top = Dimens.grid_0_25,
                        bottom = Dimens.grid_1_5
                    ),
                    text = item.size.toString(),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}

@PreviewAll
@Composable
private fun PreviewBrowserScreen() {
    Preview {
        RepositoryBrowserScreen(
            state = previewState
        )
    }
}


private val browserItems = listOf(
    RepositoryItemUiModel(
        path = "Stuff",
        size = 12,
        isFolder = true
    ),
    RepositoryItemUiModel(
        path = "fileOne",
        size = 1,
        isFolder = false
    ),
    RepositoryItemUiModel(
        path = "file2",
        size = 2,
        isFolder = false
    ),
)

val previewState = RepositoryBrowserScreenState(
    path = mutableStateOf("/path/to/folder"),
    itemCount = mutableStateOf(browserItems.size),
    repositoryItems =  mutableStateOf(browserItems),
    onRepositoryItemClicked = {}
)


