package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.navdrawer.NavDrawerState
import com.bottlerocketstudios.compose.navdrawer.NavItemState

@Composable
fun ComposeActivityViewModel.toNavDrawerState(
    items: State<List<NavItemState>>,
    devOptionsListener: () -> Unit,
) = NavDrawerState(
    avatarUrl = avatarUrl.collectAsState(),
    displayName = displayName.collectAsState(),
    username = username.collectAsState(),
    items = items,
    devOptionsListener = devOptionsListener,
)
