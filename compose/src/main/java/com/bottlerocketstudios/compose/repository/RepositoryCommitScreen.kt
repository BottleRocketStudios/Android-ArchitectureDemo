package com.bottlerocketstudios.compose.repository

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import java.time.Clock
import java.time.ZonedDateTime

@Composable
fun RepositoryCommitScreen(state: RepositoryCommitScreenState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HeaderItem(
                path = state.path.value,
                itemCount = state.itemCount.value
            )
        }
        items(items = state.commitItems.value) {
            CommitItem(
                item = it
            )
        }
    }
}

@Composable
fun CommitItem(item: RepositoryCommitItemUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = Dimens.grid_1,
            vertical = Dimens.grid_0_5
        ),
        elevation = Dimens.plane_3
    ) {
        Row {
            Icon(
                modifier = Modifier.padding(
                    start = Dimens.grid_2_5,
                    top = Dimens.grid_2
                ),
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = null,
                tint = Colors.tertiary
            )
            Column(
                modifier = Modifier.padding(
                    start = Dimens.grid_2_5,
                    top = Dimens.grid_2,
                    bottom = Dimens.grid_2
                )
            ) {
                Text(
                    text = item.author,
                    style = MaterialTheme.typography.h3.copy(color = Colors.tertiary)
                )
                Row {
                    Text(
                        text = item.branchName,
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    )
                }
                Text(
                    text = item.message,
                    style = MaterialTheme.typography.h5
                )
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(
                    top = Dimens.grid_2,
                    end = Dimens.grid_2
                ),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = item.timeSinceCommitted.getString(),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = item.hash,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
@Preview
@Composable
private fun PreviewOuterScreenContent() {
    Preview {
        RepositoryCommitScreen(
            state = RepositoryCommitScreenState(
                path = remember { mutableStateOf("/path/to/folder") },
                itemCount = remember { mutableStateOf(commitItems.size) },
                commitItems = remember {
                    mutableStateOf(commitItems)
                }
            )
        )
    }
}

private val commitItems = listOf(
    RepositoryCommitItemUiModel(
        author = "Stuff",
        timeSinceCommitted = ZonedDateTime.now().formattedUpdateTime(Clock.systemDefaultZone()),
        hash = "ygdg872rsdfs",
        message = "Test Message",
        branchName = "master"
    )
)
