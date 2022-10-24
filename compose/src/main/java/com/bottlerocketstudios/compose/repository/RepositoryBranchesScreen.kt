package com.bottlerocketstudios.compose.repository

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.resources.transparent
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import com.bottlerocketstudios.launchpad.compose.bold
import java.time.Clock
import java.time.ZonedDateTime

@Composable
fun RepositoryBranchesScreen(state: RepositoryBranchesScreenState) {
    val searchState = remember {
        mutableStateOf(TextFieldValue())
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HeaderItem(
                path = state.path.value,
                itemCount = state.itemCount.value
            )
        }
        item {
            SearchView(state = searchState, placeHolder = stringResource(id = R.string.home_branches_search))
        }
        items(items = state.branchItems.value.filter { it.name.contains(searchState.value.text) }) {
            BranchItem(
                item = it
            )
        }
    }
}

@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .padding(Dimens.grid_0_25)
            .border(2.dp, brown_grey, shape = RoundedCornerShape(4.dp))
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            placeholder = { Text(placeHolder) },
            textStyle = LocalTextStyle.current.copy(color = Colors.onBackground),
            value = state.value,
            onValueChange = { searchStr ->
                if (!searchStr.text.contains("\n")) {
                    state.value = searchStr
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = transparent,
                unfocusedIndicatorColor = transparent,
                cursorColor = Colors.onBackground
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
    }
}

@Composable
fun BranchItem(item: RepositoryBranchItemUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
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
                painter = painterResource(id = R.drawable.ic_pull_request),
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
                    text = item.name,
                    style = MaterialTheme.typography.h3.copy(color = Colors.tertiary),
                    modifier = Modifier.padding(bottom = Dimens.grid_0_5)
                )
                Text(
                    text = item.status.getString(),
                    style = MaterialTheme.typography.h5.bold()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = Dimens.grid_2,
                        end = Dimens.grid_2
                    ),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = item.timeSinceCreated.getString(),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}

@PreviewAll
@Composable
private fun PreviewOuterScreenContent() {
    Preview {
        RepositoryCommitScreen(
            state = RepositoryCommitScreenState(
                path = remember { mutableStateOf("/path/to/folder") },
                itemCount = remember { mutableStateOf(commitItems.size) },
                commitItems = remember {
                    mutableStateOf(commitItems)
                },
                branchNames = remember {
                    mutableStateOf(listOf("Master"))
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
