package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.appbar.ArchAppBarState

@Composable
fun ComposeActivityViewModel.toArchAppBarState() = ArchAppBarState(
    showToolbar = showToolbar.collectAsState(true),
    topLevel = topLevel.collectAsState(),
    title = title.collectAsState()
)
