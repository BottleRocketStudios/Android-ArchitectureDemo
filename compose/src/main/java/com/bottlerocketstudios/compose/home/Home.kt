package com.bottlerocketstudios.compose.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class HomeScreenState(
    val repositories: State<List<UserReposioryUiModel>>,
    val onRepositoryClicked: (String) -> Unit
)

@Composable
fun HomeScreen(state: HomeScreenState) {

}
