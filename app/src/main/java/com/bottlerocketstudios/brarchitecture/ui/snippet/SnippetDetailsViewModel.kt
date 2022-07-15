package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUiModel
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
    val userAvatar = MutableStateFlow(repo.user.value?.linksDto?.avatar?.href)
    val snippetTitle = MutableStateFlow("")
    val createdMessage = MutableStateFlow("")
    val updatedMessage = MutableStateFlow("")
    val isPrivate = MutableStateFlow(false)
    val files = MutableStateFlow(emptyList<SnippetDetailsFile?>())
    val owner = MutableStateFlow<User?>(null)
    val creator = MutableStateFlow<User?>(null)

    private fun getSnippetDetails(snippet: SnippetUiModel) {
        launchIO {
            if (snippet.workspaceId.isNotEmpty() && snippet.id.isNotEmpty())
                when (val result = repo.getSnippetDetails(snippet.workspaceId, snippet.id)) {
                    is Status.Success -> setSnippetData(result.data.convertToUiModel())
                    is Status.Failure -> handleError(R.string.snippets_error)
                }
        }
    }

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
