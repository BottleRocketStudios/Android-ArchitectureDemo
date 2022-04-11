package com.bottlerocketstudios.compose.profile

import androidx.compose.runtime.mutableStateOf

internal val profileMockData = ProfileScreenState(
    avatarUrl = mutableStateOf(""),
    displayName = mutableStateOf("Bob Ross"),
    nickname = mutableStateOf("Bob Ross"),
    onEditClicked = { },
    onLogoutClicked = { }
)
