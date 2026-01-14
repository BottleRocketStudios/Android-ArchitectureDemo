package com.bottlerocketstudios.compose.snippets

import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.util.toStringIdHelper

object SnippetsMockData {
    val snippetUiModel = SnippetUiModel(
        id = "1",
        workspaceId = "workspace_id",
        title = "Title",
        userName = "User Name",
        formattedLastUpdatedTime = "10 min ago".toStringIdHelper()
    )
}

@Preview
fun snippetsMockDataPreview() {
    // Preview
}
