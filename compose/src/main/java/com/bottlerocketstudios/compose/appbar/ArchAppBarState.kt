package com.bottlerocketstudios.compose.appbar

import androidx.compose.runtime.State

data class ArchAppBarState(
    val showToolbar: State<Boolean>,
    val topLevel: State<Boolean>,
    val title: State<String>,
)
