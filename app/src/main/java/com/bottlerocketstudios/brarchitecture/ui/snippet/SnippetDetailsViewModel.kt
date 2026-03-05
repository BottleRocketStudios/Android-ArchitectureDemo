package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.inject

@Suppress("TooManyFunctions")
class SnippetDetailsViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    // endregion

    // region UI State
    private val workspaceId = MutableStateFlow("")
    private val encodedId = MutableStateFlow("")

    // User
    val currentUser = repo.user
    val isWatchingSnippet = MutableStateFlow(false)

    // Snippet
    val snippetDetails = MutableStateFlow<SnippetDetails?>(null)
    val snippetFiles = MutableStateFlow(mutableListOf<SnippetDetailsFile>())
    val snippetComments = MutableStateFlow<List<SnippetComment>>(mutableListOf())

    // Comment onChange Values
    val newSnippetComment = MutableStateFlow("")
    val newReplyComment = MutableStateFlow("")
    // endregion

    // region API Calls
    fun getSnippetDetails(snippet: SnippetUiModel) = launchIO {
        if (snippet.workspaceId.isNotEmpty() && snippet.id.isNotEmpty()) {
            showLoadingIndicator.wrapIndicator {
                repo.getSnippetDetails(snippet.workspaceId, snippet.id)
                        .onSuccess { details ->
                            snippetDetails.value = details
                            workspaceId.value = snippet.workspaceId
                            encodedId.value = snippet.id
                            isUserWatchingSnippet()
                            getSnippetComments()
                            details.files?.map { file -> file.fileName }?.let { fileNameList ->
                                getRawFiles(fileNameList)
                            }
                        }
                        .onFailureLogged(errorStrId = R.string.snippets_error)
            }
        }
    }

    private fun getRawFiles(filePaths: List<String>) = launchIO {
        showLoadingIndicator.wrapIndicator {
            snippetFiles.value =
                    filePaths
                            .map { path ->
                                var rawFile: ByteArray = ByteArray(1)
                                repo.getSnippetFile(workspaceId.value, encodedId.value, path)
                                        .onSuccess { rawFile = it }
                                        .onFailureLogged(errorStrId = R.string.error_loading_file)
                                SnippetDetailsFile(fileName = path, rawFile = rawFile)
                            }
                            .toMutableList()
        }
    }

    /**
     * Coded Response: Api returns 204 if user is watching and a 404 if user is not, else an error
     * has occurred
     */
    private fun isUserWatchingSnippet() {
        launchIO {
            val result = repo.isUserWatchingSnippet(workspaceId.value, encodedId.value)
            result.onSuccess { isWatchingSnippet.value = true }.onFailure { error ->
                // TODO update to pass error code securely, but exception doesn't hold HTTP code.
                if (error.message?.contains("404") == true ||
                                error.message?.contains("Not Found") == true
                ) {
                    isWatchingSnippet.value = false
                } else {
                    handleError(R.string.snippet_watching_error)
                }
            }
        }
    }

    private fun getSnippetComments() = launchIO {
        repo.getSnippetComments(workspaceId.value, encodedId.value)
                .onSuccess { commentList -> sortComments(commentList) }
                .onFailureLogged(errorStrId = R.string.snippet_comments_error)
    }

    private fun stopWatchingSnippet() = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.stopWatchingSnippet(workspaceId.value, encodedId.value)
                    .onSuccess { isUserWatchingSnippet() }
                    .onFailureLogged(errorStrId = R.string.error_changing_watching)
        }
    }

    private fun startWatchingSnippet() = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.startWatchingSnippet(workspaceId.value, encodedId.value)
                    .onSuccess { isUserWatchingSnippet() }
                    .onFailureLogged(errorStrId = R.string.error_changing_watching)
        }
    }

    // TODO: Show dialog to confirm user wants to continue with deletion before calling this
    // function
    fun onDeleteSnippetClick() = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.deleteSnippet(workspaceId.value, encodedId.value)
                    .onSuccess { notifyUser(R.string.delete_snippet_success) }
                    .onFailureLogged(errorStrId = R.string.delete_snippet_error)
        }
    }

    private fun createSnippetComment() = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.createSnippetComment(workspaceId.value, encodedId.value, newSnippetComment.value)
                    .onSuccess {
                        getSnippetComments()
                        clearCommentValues()
                    }
                    .onFailureLogged(errorStrId = R.string.create_comment_error)
        }
    }

    private fun createReplyComment(commentId: Int) = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.createCommentReply(
                            workspaceId.value,
                            encodedId.value,
                            newReplyComment.value,
                            commentId
                    )
                    .onSuccess {
                        getSnippetComments()
                        clearCommentValues()
                    }
                    .onFailureLogged(errorStrId = R.string.comment_reply_error)
        }
    }

    fun commentEditClick(commentId: Int) = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.editSnippetComment(
                            workspaceId.value,
                            encodedId.value,
                            newSnippetComment.value,
                            commentId
                    )
                    .onSuccess { getSnippetComments() }
                    .onFailureLogged(errorStrId = R.string.edit_comment_error)
        }
    }

    fun commentDeleteClick(commentId: Int) = launchIO {
        showLoadingIndicator.wrapIndicator {
            repo.deleteSnippetComment(workspaceId.value, encodedId.value, commentId)
                    .onSuccess { getSnippetComments() }
                    .onFailureLogged(errorStrId = R.string.delete_comment_error)
        }
    }
    // endregion

    // region UI Callbacks
    fun changeSnippetWatching() =
            when (isWatchingSnippet.value) {
                true -> stopWatchingSnippet()
                false -> startWatchingSnippet()
            }

    /**
     * https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-put
     */
    fun onEditSnippetClick() {
        // TODO: Functionality not yet implemented.
        //  Clicking the edit button should show "save" and "cancel" buttons; allow the user to edit
        // the snippet name and/or
        //  delete files.
        //  When the user "saves" this function should be called and updates the snippet edits
        //  PUT /2.0/snippets/{workspace}/{encoded_id}
    }

    fun onCommentSaveEvent(commentId: Int?) {
        when (commentId == null) {
            true -> createSnippetComment()
            false -> createReplyComment(commentId)
        }
    }
    // endregion

    // region Helper Functions
    fun clearCommentValues() {
        newSnippetComment.value = ""
        newReplyComment.value = ""
    }

    private fun sortComments(comments: List<SnippetComment>) {
        val (parentComments, childrenComments) = comments.partition { it.parentId == null }

        childrenComments.forEach { child ->
            childrenComments.find { it.id == child.parentId }?.childrenComments?.add(child)
                    ?: run {
                        parentComments
                                .find { it.id == child.parentId }
                                ?.childrenComments
                                ?.add(child)
                    }
        }

        snippetComments.value = parentComments.reversed()
    }

    /** Functions for optimized sort theory. Keeping for example purposes. */
    @Suppress("UnusedPrivateMember")
    private fun sortWithRecursion(comments: List<SnippetComment>) {
        val sortedComments = comments.filter { it.parentId == null }.toMutableList()
        val unsortedComments = comments.filter { it.parentId != null }.toMutableList()

        recursiveSort(sortedComments, unsortedComments)
        snippetComments.value = sortedComments.reversed()
    }

    @Suppress("NestedBlockDepth", "UnusedPrivateMember")
    private fun recursiveSort(
            sortedComments: MutableList<SnippetComment>,
            unsortedComments: MutableList<SnippetComment>
    ) {
        if (unsortedComments.isNotEmpty()) {
            sortedComments.forEach { parent ->
                val toBeRemoved = mutableListOf<SnippetComment>()
                unsortedComments.forEach { child ->
                    if (child.parentId == parent.id) {
                        parent.childrenComments.add(child)
                        toBeRemoved.add(child)
                    }
                }
                unsortedComments.removeAll(toBeRemoved)
                recursiveSort(parent.childrenComments, unsortedComments)
            }
        }
    }
    // endregion
}
