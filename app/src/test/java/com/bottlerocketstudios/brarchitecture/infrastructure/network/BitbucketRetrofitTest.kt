package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class BasicAuthRepositoryTest {

    @Test
    fun getRepositories() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val repo = TokenAuthRepository(retrofit)
        var interceptor: Interceptor? = null
        runBlocking {
            interceptor = repo.authInterceptor("patentlychris@gmail.com", "password1")
        }
        assert(interceptor != null)
        interceptor?.let {
            val bitbucketRetrofit = BitbucketRetrofit.getRetrofit(it)
            runBlocking {
                val response = bitbucketRetrofit.getRepositories("patentlychris").execute().body()
                assert(response != null)
                response?.let {
                    assert(it.values != null)
                    it.values?.let {
                        val privateRepo = it.find { it.name == "private" }
                        assert(privateRepo != null)
                        assert(privateRepo?.is_private != null)
                        assert(privateRepo?.is_private == true)
                    }
                }
            }
        }
    }
}
