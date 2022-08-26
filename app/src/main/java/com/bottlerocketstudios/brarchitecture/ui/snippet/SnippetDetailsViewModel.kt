package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.converter.convertToComment
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUiModel
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.inject
import java.net.HttpURLConnection

@Suppress("TooManyFunctions")
class SnippetDetailsViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    // UI
    private val workspaceId = MutableStateFlow("")
    private val encodedId = MutableStateFlow("")

    // UI - User
    val currentUser = MutableStateFlow<User?>(null)
    val isWatchingSnippet = MutableStateFlow(false)

    // UI - Snippet
    val snippetDetails = MutableStateFlow<SnippetDetails?>(null)
    val snippetFiles = MutableStateFlow(mutableListOf<SnippetDetailsFile>())
    val snippetComments = MutableStateFlow<List<SnippetComment>>(mutableListOf())

    // UI - Comment onChange Values
    val newSnippetComment = MutableStateFlow("")
    val newReplyComment = MutableStateFlow("")

    init {
        currentUser.value = repo.user.value?.convertToUser()
    }

    // ///////////////////  API Calls /////////////////////
    fun getSnippetDetails(snippet: SnippetUiModel) = launchIO {
        if (snippet.workspaceId.isNotEmpty() && snippet.id.isNotEmpty()) {
            repo.getSnippetDetails(snippet.workspaceId, snippet.id).handlingErrors(R.string.snippets_error) { snippetDetailsDto ->
                workspaceId.value = snippet.workspaceId
                encodedId.value = snippet.id
                snippetDetails.value = snippetDetailsDto.convertToUiModel() // Pass just UI model
                getSnippetComments()
                snippetDetailsDto.files?.keys?.toList()?.let { getRawFiles(it) }
                isUserWatchingSnippet()
            }
        }
    }

    private fun getRawFiles(filePaths: List<String>) = launchIO {
        snippetFiles.value = filePaths.map { path ->
            var rawFile: ByteArray = ByteArray(1)
            repo.getSnippetFile(workspaceId.value, encodedId.value, path)
                .handlingErrors(R.string.error_loading_file) { rawFile = it }
            SnippetDetailsFile(fileName = path, rawFile = rawFile)
        }.toMutableList()
    }

    /** Coded Response: Api returns 204 if user is watching and a 404 if user is not, else an error has occurred */
    private fun isUserWatchingSnippet() {
        launchIO {
            when (val result = repo.isUserWatchingSnippet(workspaceId.value, encodedId.value)) {
                is Status.Success -> isWatchingSnippet.value = true
                is Status.Failure.Server ->
                    if (result.error?.httpErrorCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        isWatchingSnippet.value = false
                    } else {
                        handleError(R.string.snippet_watching_error)
                    }
                else -> handleError(R.string.snippet_watching_error)
            }
        }
    }

    private fun getSnippetComments() = launchIO {
        repo.getSnippetComments(workspaceId.value, encodedId.value).handlingErrors(R.string.snippet_comments_error) { commentList ->
            sortComments(commentList.map { it.convertToComment() })
        }
    }

    private fun stopWatchingSnippet() = launchIO {
        repo.stopWatchingSnippet(workspaceId.value, encodedId.value).handlingErrors(R.string.error_changing_watching) { isUserWatchingSnippet() }
    }

    private fun startWatchingSnippet() = launchIO {
        repo.startWatchingSnippet(workspaceId.value, encodedId.value).handlingErrors(R.string.error_changing_watching) { isUserWatchingSnippet() }
    }

    // TODO: Show dialog to confirm user wants to continue with deletion before calling this function
    fun onDeleteSnippetClick() = launchIO {
        repo.deleteSnippet(workspaceId.value, encodedId.value).handlingErrors(R.string.delete_snippet_error) {
            notifyUser(R.string.delete_snippet_success)
        }
    }

    private fun createSnippetComment() = launchIO {
        repo.createSnippetComment(workspaceId.value, encodedId.value, newSnippetComment.value).handlingErrors(R.string.create_comment_error) {
            getSnippetComments()
            clearCommentValues()
        }
    }

    private fun createReplyComment(commentId: Int) = launchIO {
        repo.createCommentReply(workspaceId.value, encodedId.value, newReplyComment.value, commentId).handlingErrors(R.string.comment_reply_error) {
            getSnippetComments()
            clearCommentValues()
        }
    }

    fun commentEditClick(commentId: Int) = launchIO {
        repo.editSnippetComment(workspaceId.value, encodedId.value, newSnippetComment.value, commentId).handlingErrors(R.string.edit_comment_error) { getSnippetComments() }
    }

    fun commentDeleteClick(commentId: Int) = launchIO {
        repo.deleteSnippetComment(workspaceId.value, encodedId.value, commentId).handlingErrors(R.string.delete_comment_error) { getSnippetComments() }
    }

    // ///////////////////  Callbacks /////////////////////
    fun changeSnippetWatching() = when (isWatchingSnippet.value) {
        true -> stopWatchingSnippet()
        false -> startWatchingSnippet()
    }

    /** https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-put */
    fun onEditSnippetClick() {
        // TODO: Functionality not yet implemented.
        //  Clicking the edit button should show "save" and "cancel" buttons; allow the user to edit the snippet name and/or
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

    // ///////////////////  Helper Functions /////////////////////
    fun clearCommentValues() {
        newSnippetComment.value = ""
        newReplyComment.value = ""
    }

    private fun sortComments(comments: List<SnippetComment>) {
        val (parentComments, childrenComments) = comments.partition { it.parentId == null }

        childrenComments.forEach { child ->
            childrenComments.find { it.id == child.parentId }?.childrenComments?.add(child) ?: run {
                parentComments.find { it.id == child.parentId }?.childrenComments?.add(child)
            }
        }

        snippetComments.value = parentComments.reversed()
    }

    @Suppress("UnusedPrivateMember")
    /** Functions for optimized sort theory. */
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
}
