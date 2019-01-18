package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface BitbucketRetrofit {
    @GET(value="2.0/repositories/{owner}")
    fun getRepositories(
        @Path(value="owner") owner: String
    ) : Call<BitbucketResponse<List<Repository>>>
    
    companion object {
        fun getRetrofit(interceptor: Interceptor): BitbucketRetrofit {
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bitbucket.org")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            return retrofit.create(BitbucketRetrofit::class.java)
        }
    }
}