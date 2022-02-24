package com.bottlerocketstudios.compose.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class FileBrowserScreenState(
    val path: State<String>,
    val content: State<String>
)

@Composable
fun FileBrowserScreen(state: FileBrowserScreenState) {

}
