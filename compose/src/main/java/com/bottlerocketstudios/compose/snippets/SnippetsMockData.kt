package com.bottlerocketstudios.compose.snippets

import com.bottlerocketstudios.compose.util.StringIdHelper

internal val mockSnippet1 = SnippetUiModel(
    id = "123456789XYZ",
    workspaceId = "workspace_slug",
    title = "Private",
    userName = "Bob Ross",
    formattedLastUpdatedTime = StringIdHelper.Raw(""),
)

internal val listOfMockSnippets = listOf(mockSnippet1, mockSnippet1)
