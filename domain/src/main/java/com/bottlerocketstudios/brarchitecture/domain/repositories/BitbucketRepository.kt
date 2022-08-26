package com.bottlerocketstudios.brarchitecture.domain.repositories

import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import com.bottlerocketstudios.brarchitecture.domain.models.Commit
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.models.PullRequest
import com.bottlerocketstudios.brarchitecture.domain.models.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.models.Snippet
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
interface BitbucketRepository : com.bottlerocketstudios.brarchitecture.domain.models.Repository {
    val user: Flow<User?>
    val repos: Flow<List<GitRepository>>
    val snippets: Flow<List<Snippet>>
    val pullRequests: Flow<List<PullRequest>>
    suspend fun authenticate(creds: ValidCredentialModel? = null): Boolean
    suspend fun authenticate(authCode: String): Boolean
    suspend fun refreshUser(): Status<Unit>
    suspend fun refreshMyRepos(): Status<Unit>
    suspend fun refreshMySnippets(): Status<Unit>
    suspend fun getRepositories(workspaceSlug: String): Status<List<GitRepository>>
    suspend fun getRepository(workspaceSlug: String, repo: String): Status<GitRepository>
    suspend fun getSource(workspaceSlug: String, repo: String): Status<List<RepoFile>>
    suspend fun getCommits(workspaceSlug: String, repo: String, branch: String): Status<List<Commit>>
    suspend fun getBranches(workspaceSlug: String, repo: String): Status<List<Branch>>
    suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): Status<List<RepoFile>>
    suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): Status<ByteArray>
    suspend fun getPullRequests(): Status<List<PullRequest>>
    suspend fun getPullRequestsWithQuery(state: String): Status<List<PullRequest>>
    suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): Status<Unit>
    suspend fun deleteSnippet(workspaceId: String, encodedId: String): Status<Unit>
    suspend fun getSnippetDetails(workspaceId: String, encodedId: String): Status<SnippetDetails>
    suspend fun getSnippetComments(workspaceId: String, encodedId: String): Status<List<SnippetComment>>
    suspend fun createSnippetComment(workspaceId: String, encodedId: String, comment: String): Status<Unit>
    suspend fun createCommentReply(workspaceId: String, encodedId: String, comment: String, commentId: Int): Status<Unit>
    suspend fun editSnippetComment(workspaceId: String, encodedId: String, comment: String, commentId: Int): Status<Unit>
    suspend fun deleteSnippetComment(workspaceId: String, encodedId: String, commentId: Int): Status<Unit>
    suspend fun getSnippetFile(workspaceId: String, encodedId: String, filePath: String): Status<ByteArray>
    suspend fun isUserWatchingSnippet(workspaceId: String, encodedId: String): Status<Int>
    suspend fun startWatchingSnippet(workspaceId: String, encodedId: String): Status<Unit>
    suspend fun stopWatchingSnippet(workspaceId: String, encodedId: String): Status<Unit>
    fun clear()
}
