package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.converter.convertToComment
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUiModel
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
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
    private val workspaceId by lazy { MutableStateFlow("") }
    private val encodedId by lazy { MutableStateFlow("") }

    // State - User
    val currentUser = MutableStateFlow<User?>(null)
    val isWatchingSnippet = MutableStateFlow(false)

    // State - Snippet
    val snippetTitle = MutableStateFlow("")
    val createdMessage = MutableStateFlow("")
    val updatedMessage = MutableStateFlow("")
    val isPrivate = MutableStateFlow(false)
    val files = MutableStateFlow(emptyList<SnippetDetailsFile?>())
    val owner = MutableStateFlow<User?>(null)
    val creator = MutableStateFlow<User?>(null)
    val httpsCloneLink = MutableStateFlow("")
    val sshCloneLink = MutableStateFlow("")
    val snippetComments = MutableStateFlow<List<SnippetComment>>(mutableListOf())

    // State - Edits
    val newSnippetComment = MutableStateFlow("")

    init {
        currentUser.value = repo.user.value?.convertToUser()
    }

    // API calls
    fun getSnippetDetails(snippet: SnippetUiModel) {
        launchIO {
            if (snippet.workspaceId.isNotEmpty() && snippet.id.isNotEmpty())
                when (val result = repo.getSnippetDetails(snippet.workspaceId, snippet.id)) {
                    is Status.Success -> {
                        workspaceId.value = snippet.workspaceId
                        encodedId.value = snippet.id
                        setSnippetData(result.data.convertToUiModel())
                        getSnippetComments()
                        result.data.files?.keys?.toList()?.let { getRawFiles(it) }
                        isUserWatchingSnippet()
                    }
                    is Status.Failure -> handleError(R.string.snippets_error)
                }
        }
    }

    private fun getRawFiles(filePaths: List<String>) {
        launchIO {
            val filesList = mutableListOf<SnippetDetailsFile>()
            filePaths.forEach { path ->
                when (val result = repo.getSnippetFile(workspaceId.value, encodedId.value, path)) {
                    is Status.Success -> filesList.add(SnippetDetailsFile(fileName = path, links = null, result.data))
                    is Status.Failure -> handleError(R.string.error_loading_file)
                }
            }
            files.value = filesList
        }
    }

    private fun isUserWatchingSnippet() {
        launchIO {
            when (repo.isUserWatchingSnippet(workspaceId.value, encodedId.value)) {
                is Status.Success -> isWatchingSnippet.value = true
                is Status.Failure.Server -> isWatchingSnippet.value = false
                else -> handleError(R.string.snippet_watching_error)
            }
        }
    }

    private fun getSnippetComments() {
        launchIO {
            when (val result = repo.getSnippetComments(workspaceId.value, encodedId.value)) {
                is Status.Success -> organizeComments(result.data.map { it.convertToComment() })
                is Status.Failure -> handleError(R.string.snippet_comments_error)
            }
        }
    }

    // Call back from button clicks
    fun changeSnippetWatching() {
        launchIO {
            when (isWatchingSnippet.value) {
                true -> stopWatchingSnippet()
                false -> startWatchingSnippet()
            }
        }
    }

    private fun stopWatchingSnippet() {
        launchIO {
            when (repo.stopWatchingSnippet(workspaceId.value, encodedId.value)) {
                is Status.Success -> isUserWatchingSnippet()
                is Status.Failure -> handleError(R.string.error_changing_watching)
            }
        }
    }

    private fun startWatchingSnippet() {
        launchIO {
            when (repo.startWatchingSnippet(workspaceId.value, encodedId.value)) {
                is Status.Success -> isUserWatchingSnippet()
                is Status.Failure -> handleError(R.string.error_changing_watching)
            }
        }
    }

    fun editSnippet() {
        Unit
    }

    fun deleteSnippet() {
        launchIO {
            when (repo.deleteSnippet(workspaceId.value, encodedId.value)) {
                is Status.Success -> {
                    // Dialog to confirm, then navigate?

                    // Using handleError to give the user a confirmation
                    handleError(R.string.delete_snippet_success)
                }
                else -> handleError(R.string.delete_snippet_error)
            }
        }
    }

    fun copyHttps() {}
    fun copySsh() {}

    fun createSnippetComment() {
        if (newSnippetComment.value.isNotEmpty()) {
            launchIO {
                when (val result = repo.createSnippetComment(workspaceId.value, encodedId.value, newSnippetComment.value)) {
                    is Status.Success -> getSnippetComments()
                    is Status.Failure -> handleError(R.string.create_comment_error)
                }
            }
        }
    }

    // Helper
    private fun setSnippetData(snippet: SnippetDetailsUiModel) {
        snippetTitle.value = snippet.title ?: ""
        createdMessage.value = snippet.createdMessage ?: ""
        updatedMessage.value =
            if (snippet.updatedMessage != snippet.createdMessage) snippet.updatedMessage.toString() else ""
        isPrivate.value = snippet.isPrivate == true
        owner.value = snippet.owner
        creator.value = snippet.creator
        httpsCloneLink.value = snippet.links?.clone?.find { it?.name == "https" }?.href ?: ""
        sshCloneLink.value = snippet.links?.clone?.find { it?.name == "ssh" }?.href ?: ""
    }

    private fun organizeComments(comments: List<SnippetComment>) {
        val parentComments = mutableListOf<SnippetComment>()
        val childrenComments = mutableListOf<SnippetComment>()

        comments.forEach { comment ->
            if (comment.parentId != null) {
                childrenComments.add(comment)
            } else {
                parentComments.add(comment)
            }
        }

        childrenComments.forEach { child ->
            val siblingComment = childrenComments.find { it.id == child.parentId }
            if (siblingComment != null) {
                siblingComment.childrenComments.add(child)
            } else {
                parentComments.find { it.id == child.parentId }?.childrenComments?.add(child)
            }
        }

        snippetComments.value = parentComments
    }
}
