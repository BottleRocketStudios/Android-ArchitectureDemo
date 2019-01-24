package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class BitbucketRetrofitTest {
    lateinit var interceptor: Interceptor

    @Before
    fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val repo = TokenAuthRepository(retrofit)
        runBlocking {
            interceptor = repo.authInterceptor("patentlychris@gmail.com", "password1")
        }
    }

    @Test
    fun getRepository_shouldRefreshTokenAndSucceed_whenTokenExpired() {
        // This does not currently force the use of an expired token
        val bitbucketRetrofit = BitbucketRetrofit.getRetrofit(interceptor)
        runBlocking {
            val response = bitbucketRetrofit.getRepository("patentlychris", "private").execute()
            val body = response.body()
            assert(body != null)
            body?.let {privateRepo ->
                assert(privateRepo.is_private != null)
                assert(privateRepo.is_private == true)
            }
        }
    }


    @Test
    fun getRepository_shouldFail_whenForceTokenExpired() {
        val bitbucketRetrofit = BitbucketRetrofit.getRetrofit(Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header("Authorization", "Bearer mdAoLW3_ug7IPJHSdnn2s_J67sPAnxNbOvVq6ePlOszhqWBxsUUWS4v_ItvhdVnkUxaaxQKn_2jrsXVqDlg=")
                .build()
            val response = chain.proceed(newRequest)
            response
        })
        runBlocking {
            val response = bitbucketRetrofit.getRepository("patentlychris", "private").execute()
            val body = response.body()
            assert(body == null)
            val errorBody = response.errorBody()
            assert(errorBody != null)
            val bodyString = errorBody?.string()?:""
            assert(bodyString.contains("Access token expired")?:false)
            assert(response.code() == 401)
        }
    }
}
