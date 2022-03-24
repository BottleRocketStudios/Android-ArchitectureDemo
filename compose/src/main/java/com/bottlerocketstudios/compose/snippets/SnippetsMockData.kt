package com.bottlerocketstudios.compose.snippets

import java.time.ZonedDateTime

internal val snippet1 = SnippetUiModel(
    title = "Private",
    userName = "Bob Ross",
    updatedTime = ZonedDateTime.now()
)

internal val listOfMockSnippets = listOf(snippet1, snippet1)
