package com.bottlerocketstudios.brarchitecture.domain.repositories

import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import com.bottlerocketstudios.brarchitecture.domain.models.Commit
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Project
import com.bottlerocketstudios.brarchitecture.domain.models.PullRequest
import com.bottlerocketstudios.brarchitecture.domain.models.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.models.Snippet
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
interface BitbucketRepository : com.bottlerocketstudios.brarchitecture.domain.models.Repository {
        val user: Flow<User?>
        val repos: Flow<List<GitRepository>>
        val snippets: Flow<List<Snippet>>
        val pullRequests: Flow<List<PullRequest>>
        val projects: Flow<List<Project>>
        suspend fun authenticate(creds: ValidCredentialModel? = null): Boolean
        suspend fun authenticate(authCode: String): Boolean
        suspend fun refreshUser(): Result<Unit>
        suspend fun refreshMyRepos(): Result<Unit>
        suspend fun refreshMySnippets(): Result<Unit>
        suspend fun getRepositories(workspaceSlug: String): Result<List<GitRepository>>
        suspend fun getWorkspaces(): Result<List<Workspace>>
        suspend fun getRepository(workspaceSlug: String, repo: String): Result<GitRepository>
        suspend fun getSource(workspaceSlug: String, repo: String): Result<List<RepoFile>>
        suspend fun getCommits(
                workspaceSlug: String,
                repo: String,
                branch: String
        ): Result<List<Commit>>
        suspend fun getBranches(workspaceSlug: String, repo: String): Result<List<Branch>>
        suspend fun getSourceFolder(
                workspaceSlug: String,
                repo: String,
                hash: String,
                path: String
        ): Result<List<RepoFile>>
        suspend fun getSourceFile(
                workspaceSlug: String,
                repo: String,
                hash: String,
                path: String
        ): Result<ByteArray>
        suspend fun getPullRequests(workspaceSlug: String? = null): Result<List<PullRequest>>
        suspend fun getPullRequestsWithQuery(state: String): Result<List<PullRequest>>
        suspend fun createSnippet(
                title: String,
                filename: String,
                contents: String,
                private: Boolean
        ): Result<Unit>
        suspend fun deleteSnippet(workspaceId: String, encodedId: String): Result<Unit>
        suspend fun getSnippetDetails(
                workspaceId: String,
                encodedId: String
        ): Result<SnippetDetails>
        suspend fun getSnippetComments(
                workspaceId: String,
                encodedId: String
        ): Result<List<SnippetComment>>
        suspend fun createSnippetComment(
                workspaceId: String,
                encodedId: String,
                comment: String
        ): Result<Unit>
        suspend fun createCommentReply(
                workspaceId: String,
                encodedId: String,
                comment: String,
                commentId: Int
        ): Result<Unit>
        suspend fun editSnippetComment(
                workspaceId: String,
                encodedId: String,
                comment: String,
                commentId: Int
        ): Result<Unit>
        suspend fun deleteSnippetComment(
                workspaceId: String,
                encodedId: String,
                commentId: Int
        ): Result<Unit>
        suspend fun getSnippetFile(
                workspaceId: String,
                encodedId: String,
                filePath: String
        ): Result<ByteArray>
        suspend fun isUserWatchingSnippet(workspaceId: String, encodedId: String): Result<Int>
        suspend fun startWatchingSnippet(workspaceId: String, encodedId: String): Result<Unit>
        suspend fun stopWatchingSnippet(workspaceId: String, encodedId: String): Result<Unit>
        suspend fun getProjects(): Result<List<Project>>
        suspend fun clear()
}
