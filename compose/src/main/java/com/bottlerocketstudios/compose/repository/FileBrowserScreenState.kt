package com.bottlerocketstudios.compose.repository

import androidx.compose.runtime.State

data class FileBrowserScreenState(
    val path: State<String>,
    val content: State<ByteArray?>,
)
