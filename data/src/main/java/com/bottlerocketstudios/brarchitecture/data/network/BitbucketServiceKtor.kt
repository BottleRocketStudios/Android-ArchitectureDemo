package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.model.BranchDto
import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.ProjectDto
import com.bottlerocketstudios.brarchitecture.data.model.PullRequestDto
import com.bottlerocketstudios.brarchitecture.data.model.RepoFileDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Ktor based implementation of the Bitbucket API. Only a subset of endpoints are implemented
 * for demonstration purposes. Additional endpoints can be added following the same pattern.
 */
internal class BitbucketServiceKtor : KoinComponent {
    private val client: HttpClient by inject()

    suspend fun getUser(): UserDto = client.get("2.0/user").body()

    suspend fun getRepositories(workspace: String): BitbucketPagedResponse<List<GitRepositoryDto>> =
        client.get("2.0/repositories/$workspace").body()

    suspend fun getRepository(workspace: String, repo: String): GitRepositoryDto =
        client.get("2.0/repositories/$workspace/$repo").body()

    suspend fun getRepositorySource(workspace: String, repo: String): BitbucketPagedResponse<List<RepoFileDto>> =
        client.get("2.0/repositories/$workspace/$repo/src").body()

    suspend fun getRepositoryCommits(workspace: String, repo: String, branch: String): BitbucketPagedResponse<List<CommitDto>> =
        client.get("2.0/repositories/$workspace/$repo/commits/$branch").body()

    suspend fun getRepositoryBranches(workspace: String, repo: String): BitbucketPagedResponse<List<BranchDto>> =
        client.get("2.0/repositories/$workspace/$repo/refs/branches").body()

    suspend fun getRepositorySourceFolder(workspace: String, repo: String, hash: String, path: String): BitbucketPagedResponse<List<RepoFileDto>> =
        client.get("2.0/repositories/$workspace/$repo/src/$hash/$path").body()

    suspend fun getRepositorySourceFile(workspace: String, repo: String, hash: String, path: String): io.ktor.client.statement.HttpResponse =
        client.get("2.0/repositories/$workspace/$repo/src/$hash/$path")

    suspend fun getPullRequests(selectedUser: String): BitbucketPagedResponse<List<PullRequestDto>> =
        client.get("2.0/pullrequests/$selectedUser").body()

    suspend fun getPullRequestsWithQuery(selectedUser: String, state: String): BitbucketPagedResponse<List<PullRequestDto>> =
        client.get("2.0/pullrequests/$selectedUser") {
            url {
                parameters.append("state", state)
            }
        }.body()

    suspend fun getSnippets(): BitbucketPagedResponse<List<SnippetDto>> =
        client.get("2.0/snippets") {
            url {
                parameters.append("role", "owner")
            }
        }.body()

    suspend fun deleteSnippet(workspaceId: String, encodedId: String): io.ktor.client.statement.HttpResponse =
        client.delete("2.0/snippets/$workspaceId/$encodedId")

    suspend fun getSnippetDetails(workspaceId: String, encodedId: String): SnippetDetailsDto =
        client.get("2.0/snippets/$workspaceId/$encodedId").body()

    suspend fun getSnippetFile(workspaceId: String, encodedId: String, path: String): io.ktor.client.statement.HttpResponse =
        client.get("2.0/snippets/$workspaceId/$encodedId/files/$path")

    suspend fun isUserWatchingSnippet(workspaceId: String, encodedId: String): io.ktor.client.statement.HttpResponse =
        client.get("2.0/snippets/$workspaceId/$encodedId/watch")

    suspend fun startWatchingSnippet(workspaceId: String, encodedId: String): io.ktor.client.statement.HttpResponse =
        client.put("2.0/snippets/$workspaceId/$encodedId/watch")

    suspend fun stopWatchingSnippet(workspaceId: String, encodedId: String): io.ktor.client.statement.HttpResponse =
        client.delete("2.0/snippets/$workspaceId/$encodedId/watch")

    suspend fun getSnippetComments(workspaceId: String, encodedId: String): BitbucketPagedResponse<List<SnippetCommentDto>> =
        client.get("2.0/snippets/$workspaceId/$encodedId/comments").body()

    suspend fun createSnippetComment(workspaceId: String, encodedId: String, body: SnippetCommentDto): io.ktor.client.statement.HttpResponse =
        client.post("2.0/snippets/$workspaceId/$encodedId/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun createCommentReply(workspaceId: String, encodedId: String, body: SnippetCommentDto): io.ktor.client.statement.HttpResponse =
        client.post("2.0/snippets/$workspaceId/$encodedId/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun editSnippetComment(workspaceId: String, encodedId: String, commentId: Int, body: SnippetCommentDto): io.ktor.client.statement.HttpResponse =
        client.put("2.0/snippets/$workspaceId/$encodedId/comments/$commentId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun deleteSnippetComment(workspaceId: String, encodedId: String, commentId: Int): io.ktor.client.statement.HttpResponse =
        client.delete("2.0/snippets/$workspaceId/$encodedId/comments/$commentId")

    suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): SnippetDto = client.post("2.0/snippets") {
        setBody(MultiPartFormDataContent(
            formData {
                append("title", title)
                append("is_private", private.toString())
                append("file", contents, Headers.build {
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"file\"; filename=\"$filename\"")
                    append(HttpHeaders.ContentType, "text/plain")
                })
            }
        ))
    }.body()

    suspend fun getProjects(workspace: String): BitbucketPagedResponse<List<ProjectDto>> =
        client.get("2.0/workspaces/$workspace/projects").body()
}
