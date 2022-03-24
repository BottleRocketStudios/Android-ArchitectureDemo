package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
    ): Response<BitbucketPagedResponse<List<RepoFile>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src/%7Bnode%7D/%7Bpath%7D */
    @GET(value = "2.0/repositories/{workspace}/{repo}/src/{hash}/{path}")
    suspend fun getRepositorySourceFolder(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Response<BitbucketPagedResponse<List<RepoFile>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src/%7Bnode%7D/%7Bpath%7D */
    @GET(value = "2.0/repositories/{workspace}/{repo}/src/{hash}/{path}")
    suspend fun getRepositorySourceFile(
        @Path(value = "workspace") workspace: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Response<ResponseBody>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/snippets */
    @GET(value = "2.0/snippets?role=owner")
    suspend fun getSnippets(): Response<BitbucketPagedResponse<List<Snippet>>>

    @Multipart
    @POST(value = "2.0/snippets")
    suspend fun createSnippet(
        @Part("title") title: String,
        @Part body: MultipartBody.Part,
        @Part("is_private") private: Boolean
    ): Response<Snippet>
}
