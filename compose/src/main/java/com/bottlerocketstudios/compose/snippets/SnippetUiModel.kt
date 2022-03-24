package com.bottlerocketstudios.compose.snippets

import java.time.ZonedDateTime

data class SnippetUiModel(
    val title: String,
    val userName: String,
    var updatedTime: ZonedDateTime? = null,
    var updatedTimeString: String? = null,
)
