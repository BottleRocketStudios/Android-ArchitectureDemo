package com.bottlerocketstudios.compose.snippets

import com.bottlerocketstudios.compose.util.StringIdHelper

data class SnippetUiModel(
    val id: String,
    val workspaceId: String,
    val title: String,
    val userName: String,
    val formattedLastUpdatedTime: StringIdHelper,
)
