package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUiModel
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetDetailsUiModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.inject

class SnippetDetailsViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    // State
    val currentUser = MutableStateFlow<User?>(null)
    val snippetTitle = MutableStateFlow("")
    val createdMessage = MutableStateFlow("")
    val updatedMessage = MutableStateFlow("")
    val isPrivate = MutableStateFlow(false)
    val files = MutableStateFlow(emptyList<SnippetDetailsFile?>())
    val owner = MutableStateFlow<User?>(null)
    val creator = MutableStateFlow<User?>(null)

    val isWatchingSnippet = MutableStateFlow(false)

    val comment = MutableStateFlow("")

    init {
        currentUser.value = repo.user.value?.convertToUser()
    }

    // API calls
    private fun getSnippetDetails(snippet: SnippetUiModel) {
        launchIO {
            if (snippet.workspaceId.isNotEmpty() && snippet.id.isNotEmpty())
                when (val result = repo.getSnippetDetails(snippet.workspaceId, snippet.id)) {
                    is Status.Success -> setSnippetData(result.data.convertToUiModel())
                    is Status.Failure -> handleError(R.string.snippets_error)
                }
        }
    }

    private fun isWatchingSnippet() {
        // TODO: set "isWatchingSnippet"
        //  check if the current user is watching a specific snippet.
        //  GET /2.0/snippets/{workspace}/{encoded_id}/watch
    }

    private fun getSnippetComments() {
        // TODO: GET /2.0/snippets/{workspace}/{encoded_id}/comments
        //  https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-comments-get
    }

    // Call back from button clicks
    fun watchSnippet() {
        // TODO:
        //  Watch Snippet
        //  PUT /2.0/snippets/{workspace}/{encoded_id}/watch
        //  Stop Watching Snippet
        //  DELETE /2.0/snippets/{workspace}/{encoded_id}/watch
    }

    fun cloneSnippet() { Unit }
    fun editSnippet() { Unit }

    fun deleteSnippet() {
        // TODO: DELETE /2.0/snippets/{workspace}/{encoded_id}
        //  https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-delete
    }

    fun getRawFile() {
        // TODO GET /2.0/snippets/{workspace}/{encoded_id}/{node_id}/files/{path}
    }


    // Helper
    private fun setSnippetData(snippet: SnippetDetailsUiModel) {
        snippetTitle.value = snippet.title ?: ""
        createdMessage.value = snippet.createdMessage ?: ""
        updatedMessage.value = snippet.updatedMessage ?: ""
        isPrivate.value = snippet.isPrivate == true
        files.value = snippet.files ?: emptyList()
        owner.value = snippet.owner
        creator.value = snippet.creator
    }
}
