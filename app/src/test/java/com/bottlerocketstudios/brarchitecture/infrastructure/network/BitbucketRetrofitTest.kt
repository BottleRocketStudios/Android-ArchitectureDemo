package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class BasicAuthRepositoryTest {

    @Test
    fun authInterceptor() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val b = TokenAuthRepository(retrofit)
        var p: Interceptor? = null
        runBlocking {
            p = b.authInterceptor("patentlychris@gmail.com", "password1")
        }
        assert(p != null)
        p?.let {
            val bitbucketRetrofit = BitbucketRetrofit.getRetrofit(it)
            runBlocking {
                System.out.println(bitbucketRetrofit.getRepositories("patentlychris").execute().body()?.values?.get(0)?.name)
            }
        }
    }
}
