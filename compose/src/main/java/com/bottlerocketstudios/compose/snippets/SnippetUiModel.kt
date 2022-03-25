package com.bottlerocketstudios.compose.snippets

import com.bottlerocketstudios.compose.util.StringIdHelper

data class SnippetUiModel(
    val title: String,
    val userName: String,
    val formattedTime: StringIdHelper,
)
