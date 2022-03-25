package com.bottlerocketstudios.compose.snippets

import com.bottlerocketstudios.compose.util.StringIdHelper

internal val snippet1 = SnippetUiModel(
    title = "Private",
    userName = "Bob Ross",
    formattedTime = StringIdHelper.Raw(""),
)

internal val listOfMockSnippets = listOf(snippet1, snippet1)
