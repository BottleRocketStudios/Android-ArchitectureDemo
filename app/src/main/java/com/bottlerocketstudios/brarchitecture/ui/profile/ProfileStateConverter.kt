package com.bottlerocketstudios.brarchitecture.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.profile.ProfileScreenState

@Composable
fun ProfileViewModel.toState() = ProfileScreenState(
    avatarUrl = avatarUrl.collectAsState(initial = ""),
    displayName = displayName.collectAsState(initial = ""),
    nickname = nickname.collectAsState(initial = ""),
    onEditClicked = ::onEditClicked,
    onLogoutClicked = ::onLogoutClicked
)
