package com.bottlerocketstudios.compose.navdrawer

import androidx.compose.runtime.State

data class NavDrawerState(
    val avatarUrl: State<String>,
    val displayName: State<String>,
    val username: State<String>,
    val items: State<List<NavItemState>>,
    val devOptionsListener: () -> Unit,
)
