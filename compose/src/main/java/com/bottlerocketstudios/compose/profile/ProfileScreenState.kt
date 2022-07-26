package com.bottlerocketstudios.compose.profile

import androidx.compose.runtime.State

data class ProfileScreenState(
    val avatarUrl: State<String>,
    val displayName: State<String>,
    val nickname: State<String>,
    val onEditClicked: () -> Unit,
    val onLogoutClicked: () -> Unit,
)
