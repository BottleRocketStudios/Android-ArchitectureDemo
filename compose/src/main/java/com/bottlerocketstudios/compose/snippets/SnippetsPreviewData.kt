package com.bottlerocketstudios.compose.snippets

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.bottlerocketstudios.compose.util.StringIdHelper
import com.bottlerocketstudios.compose.util.asMutableState

internal val mockSnippet1 = SnippetUiModel(
    id = "123456789XYZ",
    workspaceId = "workspace_slug",
    title = "Private",
    userName = "Bob Ross",
    formattedLastUpdatedTime = StringIdHelper.Raw(""),
)

internal val listOfMockSnippets = listOf(mockSnippet1, mockSnippet1)

internal class SnippetBrowserPreviewProvider : PreviewParameterProvider<SnippetsBrowserScreenState> {
    override val values: Sequence<SnippetsBrowserScreenState> = sequenceOf(
        SnippetsBrowserScreenState(
            snippets = listOfMockSnippets.asMutableState(),
            createVisible = true.asMutableState(),
            onCreateSnippetClicked = {},
        ) {},
        SnippetsBrowserScreenState(
            snippets = listOfMockSnippets.asMutableState(),
            createVisible = false.asMutableState(),
            onCreateSnippetClicked = {},
        ) {},
        SnippetsBrowserScreenState(
            snippets = emptyList<SnippetUiModel>().asMutableState(),
            createVisible = true.asMutableState(),
            onCreateSnippetClicked = {},
        ) {},
    )
}
