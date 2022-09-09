package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.model.BranchDto
import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.PullRequestDto
import com.bottlerocketstudios.brarchitecture.data.model.RepoFileDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("TooManyFunctions")
/** Primary apis to interact with bitbucket. See https://developer.atlassian.com/bitbucket/api/2/reference */
internal interface BitbucketService {
    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/user */
    @GET(value = "2.0/user")
    suspend fun getUser(): Response<UserDto>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D */
    @GET(value = "2.0/repositories/{workspace}")
    suspend fun getRepositories(
        @Path(value = "workspace") workspace: String
    ): Response<BitbucketPagedResponse<List<GitRepositoryDto>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D */
    @GET(value = "2.0/repositories/{workspace}/{repo}")
    suspend fun getRepository(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String
    ): Response<GitRepositoryDto>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src */
    @GET(value = "2.0/repositories/{workspace}/{repo}/src")
    suspend fun getRepositorySource(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String
    ): Response<BitbucketPagedResponse<List<RepoFileDto>>>

    @GET(value = "2.0/repositories/{workspace}/{repo}/commits/{branch}")
    suspend fun getRepositoryCommits(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String,
        @Path(value = "branch") branch: String
    ): Response<BitbucketPagedResponse<List<CommitDto>>>

    /** */
    @GET(value = "https://api.bitbucket.org/2.0/repositories/{workspace}/{repo}/refs/branches")
    suspend fun getRepositoryBranches(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String
    ): Response<BitbucketPagedResponse<List<BranchDto>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src/%7Bnode%7D/%7Bpath%7D */
    @GET(value = "2.0/repositories/{workspace}/{repo}/src/{hash}/{path}")
    suspend fun getRepositorySourceFolder(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Response<BitbucketPagedResponse<List<RepoFileDto>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src/%7Bnode%7D/%7Bpath%7D */
    @GET(value = "2.0/repositories/{workspace}/{repo}/src/{hash}/{path}")
    suspend fun getRepositorySourceFile(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Response<ResponseBody>

    @GET("2.0/pullrequests/{selected_user}")
    suspend fun getPullRequests(
        @Path(value = "selected_user") selectedUser: String
    ): Response<BitbucketPagedResponse<List<PullRequestDto>>>

    @GET("2.0/pullrequests/{selected_user}")
    suspend fun getPullRequestsWithQuery(
        @Path(value = "selected_user") selectedUser: String,
        @Query(value = "state") state: String,
    ): Response<BitbucketPagedResponse<List<PullRequestDto>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/snippets */
    @GET(value = "2.0/snippets?role=owner")
    suspend fun getSnippets(): Response<BitbucketPagedResponse<List<SnippetDto>>>

    @Multipart
    @POST(value = "2.0/snippets")
    suspend fun createSnippet(
        @Part("title") title: String,
        @Part body: MultipartBody.Part,
        @Part("is_private") private: Boolean
    ): Response<SnippetDto>

    /** https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-delete */
    @DELETE(value = "/2.0/snippets/{workspace}/{encoded_id}")
    suspend fun deleteSnippet(
        @Path(value = "workspace") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String
    ): Response<Unit>

    /** https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-get */
    @GET(value = "2.0/snippets/{workspace_id}/{encoded_id}")
    suspend fun getSnippetDetails(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String
    ): Response<SnippetDetailsDto>

    @GET(value = "/2.0/snippets/{workspace_id}/{encoded_id}/files/{path}")
    suspend fun getSnippetFile(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String,
        @Path(value = "path") path: String
    ): Response<ResponseBody>

    /**https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-watch-get */
    @GET(value = "/2.0/snippets/{workspace_id}/{encoded_id}/watch")
    suspend fun isUserWatchingSnippet(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String
    ): Response<Int>

    /** https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-watch-put */
    @PUT(value = "/2.0/snippets/{workspace_id}/{encoded_id}/watch")
    suspend fun startWatchingSnippet(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String
    ): Response<Unit>

    /** https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-watch-delete */
    @DELETE(value = "/2.0/snippets/{workspace_id}/{encoded_id}/watch")
    suspend fun stopWatchingSnippet(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String
    ): Response<Unit>

    /** https://developer.atlassian.com/cloud/bitbucket/rest/api-group-snippets/#api-snippets-workspace-encoded-id-comments-get */
    @GET(value = "/2.0/snippets/{workspace_id}/{encoded_id}/comments")
    suspend fun getSnippetComments(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String
    ): Response<BitbucketPagedResponse<List<SnippetCommentDto>>>

    @POST(value = "/2.0/snippets/{workspace_id}/{encoded_id}/comments")
    suspend fun createSnippetComment(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String,
        @Body body: SnippetCommentDto,
    ): Response<Unit>

    @POST(value = "/2.0/snippets/{workspace_id}/{encoded_id}/comments")
    suspend fun createCommentReply(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String,
        @Body body: SnippetCommentDto,
    ): Response<Unit>

    @PUT(value = "/2.0/snippets/{workspace_id}/{encoded_id}/comments/{comment_id}")
    suspend fun editSnippetComment(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String,
        @Path(value = "comment_id") commentId: Int,
        @Body body: SnippetCommentDto,
    ): Response<Unit>

    @DELETE(value = "/2.0/snippets/{workspace_id}/{encoded_id}/comments/{comment_id}")
    suspend fun deleteSnippetComment(
        @Path(value = "workspace_id") workspaceId: String,
        @Path(value = "encoded_id") encodedId: String,
        @Path(value = "comment_id") commentId: Int
    ): Response<Unit>
}
