package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.domain.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/** Primary apis to interact with bitbucket */
interface BitbucketService {
    @GET(value = "2.0/user")
    fun getUser(): Call<User>

    @GET(value = "2.0/repositories/{owner}")
    fun getRepositories(
        @Path(value = "owner") owner: String
    ): Call<BitbucketPagedResponse<List<Repository>>>

    @GET(value = "2.0/repositories/{owner}/{repo}")
    fun getRepository(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String
    ): Call<Repository>

    @GET(value = "2.0/repositories/{owner}/{repo}/src")
    fun getRepositorySource(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String
    ): Call<BitbucketPagedResponse<List<RepoFile>>>

    @GET(value = "2.0/repositories/{owner}/{repo}/src/{hash}/{path}")
    fun getRepositorySourceFolder(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Call<BitbucketPagedResponse<List<RepoFile>>>

    @GET(value = "2.0/repositories/{owner}/{repo}/src/{hash}/{path}")
    fun getRepositorySourceFile(
        @Path(value = "owner") owner: String,
        @Path(value = "repo") repo: String,
        @Path(value = "hash") hash: String,
        @Path(value = "path") path: String
    ): Call<String>
}
