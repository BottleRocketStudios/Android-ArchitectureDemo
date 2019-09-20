package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.domain.model.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BitbucketRetrofit {
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

    @GET(value="2.0/repositories/{owner}/{repo}/src")
    fun getRepositorySource(
        @Path(value="owner") owner: String,
        @Path(value="repo") repo: String
    ) : Call<BitbucketPagedResponse<List<RepoFile>>>

    @GET(value="2.0/repositories/{owner}/{repo}/src/{hash}/{path}")
    fun getRepositorySourceFolder(
        @Path(value="owner") owner: String,
        @Path(value="repo") repo: String,
        @Path(value="hash") hash: String,
        @Path(value="path") path: String
    ) : Call<BitbucketPagedResponse<List<RepoFile>>>
    
    @GET(value="2.0/repositories/{owner}/{repo}/src/{hash}/{path}")
    fun getRepositorySourceFile(
        @Path(value="owner") owner: String,
        @Path(value="repo") repo: String,
        @Path(value="hash") hash: String,
        @Path(value="path") path: String
    ) : Call<String>

    companion object {
        fun getRetrofit(interceptor: Interceptor?): BitbucketRetrofit {
            val clientBuilder = OkHttpClient.Builder()
            interceptor?.let {
                clientBuilder.addInterceptor(interceptor)
            }
            val client = clientBuilder.build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bitbucket.org")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            return retrofit.create(BitbucketRetrofit::class.java)
        }
    }
}