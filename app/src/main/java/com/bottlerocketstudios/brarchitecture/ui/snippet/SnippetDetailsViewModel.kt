package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUiModel
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Snippet
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetDetailsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.inject

class SnippetDetailsViewModel: BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    // State
    // TODO: Need current GitRepository Workspace
    private val currentWorkspace: Workspace = Workspace("", "", "")
    val currentSnippet = MutableStateFlow<SnippetDetailsUiModel?>(null)


    /** When calling for details:
     * encoded_id (string) == The snippet id.

    workspace (string) == workspace ID (slug) OR workspace UUID surrounded by curly-braces, for example: {workspace UUID}.
     */

    fun getSnippetDetails(snippet: Snippet) {
        val workspaceId = currentWorkspace.slug ?: currentWorkspace.uuid
        val snippetId: String? = snippet.id
        launchIO {
            if ((workspaceId != null) && (snippetId != null)) {
                when (val result = repo.getSnippetDetails(workspaceId, snippetId)) {
                    is Status.Success -> currentSnippet.value = result.data.convertToUiModel()
                    is Status.Failure -> handleError(R.string.snippets_error)
                }
            } else {
                handleError(R.string.snippets_error)
            }
        }
    }
}
