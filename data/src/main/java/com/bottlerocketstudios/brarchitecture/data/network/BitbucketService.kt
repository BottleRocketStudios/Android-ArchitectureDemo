package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.data.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/** Primary apis to interact with bitbucket. See https://developer.atlassian.com/bitbucket/api/2/reference */
internal interface BitbucketService {
    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/user */
    @GET(value = "2.0/user")
    fun getUser(): Call<User>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D */
    @GET(value = "2.0/repositories/{owner}")
    fun getRepositories(
        @Path(value = "owner") owner: String
    ): Call<BitbucketPagedResponse<List<Repository>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D */
    @GET(value = "2.0/repositories/{owner}/{repo}")
    fun getRepository(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String
    ): Call<Repository>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src */
    @GET(value = "2.0/repositories/{owner}/{repo}/src")
    fun getRepositorySource(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String
    ): Call<BitbucketPagedResponse<List<RepoFile>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src/%7Bnode%7D/%7Bpath%7D */
    @GET(value = "2.0/repositories/{owner}/{repo}/src/{hash}/{path}")
    fun getRepositorySourceFolder(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Call<BitbucketPagedResponse<List<RepoFile>>>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src/%7Bnode%7D/%7Bpath%7D */
    @GET(value = "2.0/repositories/{owner}/{repo}/src/{hash}/{path}")
    fun getRepositorySourceFile(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Call<String>

    /** https://developer.atlassian.com/bitbucket/api/2/reference/resource/snippets */
    @GET(value = "2.0/snippets?role=owner")
    fun getSnippets(): Call<BitbucketPagedResponse<List<Snippet>>>

    @Multipart
    @POST(value = "2.0/snippets")
    fun createSnippet(
        @Part("title") title: String,
        @Part body: MultipartBody.Part,
        @Part("is_private") private: Boolean
    ): Call<Snippet>
}
