package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.bottlerocketstudios.brarchitecture.BaseTest
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber.d
import java.net.HttpURLConnection


class BitbucketRetrofitTest : BaseTest() {
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
            assertThat(body).isNotNull()
            body?.let {privateRepo ->
                assertThat(privateRepo.is_private).isNotNull()
                assertThat(privateRepo.is_private).isTrue()
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
            assertThat(body).isNull()
            val errorBody = response.errorBody()
            assertThat(errorBody).isNotNull()
            val bodyString = errorBody?.string()?:""
            assertThat(bodyString).contains("Access token expired")
            assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED)
        }
    }
}
