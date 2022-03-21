package com.bottlerocketstudios.compose.profile

import androidx.compose.runtime.mutableStateOf

val ProfileMockData = ProfileScreenState (
    avatarUrl = mutableStateOf(""),
    displayName = mutableStateOf("Bob Ross"),
    nickname = mutableStateOf("Bob Ross"),
    onEditClicked = { } ,
    onLogoutClicked = { }
)
