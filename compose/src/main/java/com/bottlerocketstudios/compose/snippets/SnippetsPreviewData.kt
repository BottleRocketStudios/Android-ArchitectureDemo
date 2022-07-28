package com.bottlerocketstudios.compose.snippets

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.bottlerocketstudios.compose.util.StringIdHelper
import com.bottlerocketstudios.compose.util.asMutableState

internal val mockSnippet1 = SnippetUiModel(
    id = "123456789XYZ",
    workspaceSlug = "workspace_slug",
    title = "Private",
    userName = "Bob Ross",
    formattedLastUpdatedTime = StringIdHelper.Raw(""),
)

internal val listOfMockSnippets = listOf(mockSnippet1, mockSnippet1)

class SnippetBrowserPreviewProvider : PreviewParameterProvider<SnippetsBrowserScreenState> {
    override val values: Sequence<SnippetsBrowserScreenState> = sequenceOf(
        SnippetsBrowserScreenState(
            snippets = listOfMockSnippets.asMutableState(),
            createVisible = true.asMutableState()
        ) {},
        SnippetsBrowserScreenState(
            snippets = listOfMockSnippets.asMutableState(),
            createVisible = false.asMutableState()
        ) {},
        SnippetsBrowserScreenState(
            snippets = emptyList<SnippetUiModel>().asMutableState(),
            createVisible = true.asMutableState()
        ) {},
    )
}
