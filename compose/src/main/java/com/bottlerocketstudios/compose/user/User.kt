package com.bottlerocketstudios.compose.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class UserScreenState(
    val avatarUrl: State<String>,
    val displayName: State<String>,
    val nickname: State<String>,
    val onEditClicked: () -> Unit,
    val onLogoutClicked: () -> Unit,
)

@Composable
fun UserScreen(state: UserScreenState) {

}
