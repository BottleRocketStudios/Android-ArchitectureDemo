package com.bottlerocketstudios.compose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.asState

data class HomeScreenState(
    val repositories: State<List<UserRepositoryUiModel>>
)

@Composable
fun HomeScreen(state: HomeScreenState, selectItem: (userRepositoryUiModel: UserRepositoryUiModel) -> Unit) {
    Surface {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
            modifier = Modifier
                .padding(
                    start = Dimens.grid_1_5,
                    end = Dimens.grid_1_5
                )
                .fillMaxSize()
        ) {
            state.repositories.value.forEach {
                item {
                    CardLayout(it, selectItem)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Preview {
        HomeScreen(
            state = HomeScreenState(
                listOf(testCard1, testCard2).asState()
            )
        ) {}
    }
}

