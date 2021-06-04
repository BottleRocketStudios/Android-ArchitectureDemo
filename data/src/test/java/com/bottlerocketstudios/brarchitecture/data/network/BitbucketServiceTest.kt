package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import com.bottlerocketstudios.brarchitecture.data.repository.DateTimeAdapter
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.HttpURLConnection

class BitbucketServiceTest : BaseTest() {

    @Test
    @Ignore("Test only works if actual usable tokens are provided")
    fun getRepository_shouldRefreshTokenAndSucceed_whenTokenExpired() {
        // This does not currently force the use of an expired token
        val accessToken = AccessToken(
            accessToken = "REPLACE WITH REAL TOKEN",
            refreshToken = "REPLACE WITH REAL TOKEN",
            expiresInSeconds = 7200,
            tokenType = "bearer",
            scopes = "project account pullrequest"
        )
        runBlocking {
            val bitbucketService = createBitbucketService(accessToken)
            val response = bitbucketService.getRepository("REPLACE WITH USERNAME", "REPLACE WITH PRIVATE REPOSITORY")
            val body = response.body()
            assertThat(body).isNotNull()
            body?.let { privateRepo: Repository ->
                assertThat(privateRepo.isPrivate).isNotNull()
                assertThat(privateRepo.isPrivate).isTrue()
            }
        }
    }

    @Test
    @Ignore("Test only works if actual expired tokens are provided")
    fun getRepository_shouldFail_whenForceTokenExpired() {
        val accessToken = AccessToken(
            accessToken = "REPLACE WITH REAL TOKEN",
            refreshToken = "REPLACE WITH REAL TOKEN",
            expiresInSeconds = 7200,
            tokenType = "bearer",
            scopes = "project account pullrequest"
        )
        runBlocking {
            val bitbucketService = createBitbucketService(accessToken)
            val response = bitbucketService.getRepository("REPLACE WITH USERNAME", "REPLACE WITH PRIVATE REPOSITORY")
            val body = response.body()
            assertThat(body).isNull()
            val errorBody = response.errorBody()
            assertThat(errorBody).isNotNull()
            val bodyString = errorBody?.string() ?: ""
            assertThat(bodyString).containsMatch("Access token expired|Access denied")
            assertThat(response.code()).isAnyOf(HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_FORBIDDEN)
        }
    }

    @Test
    fun getUser_shouldReturnUser_whenAuthenticated() {
        runBlocking {
            val bitbucketService = createBitbucketService(null)
            val response = bitbucketService.getUser()
            val body = response.body()
            val errorBody = response.errorBody()
        }
    }

    // TODO: Consider adding koin-test and use a test koin graph instead of the manual creation here
    private suspend fun createBitbucketService(accessToken: AccessToken?): BitbucketService {
        val bitbucketCredentialsRepository = mock<BitbucketCredentialsRepository> {
            on { loadCredentials() } doReturn ValidCredentialModel("username", "password")
            on { loadToken() } doReturn accessToken
        }

        val unauthOkHttpClient = OkHttpClient.Builder().build()
        val unathRetrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .client(unauthOkHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val authService = unathRetrofit.create(TokenAuthService::class.java)
        val interceptor = TokenAuthInterceptor(authService, bitbucketCredentialsRepository)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.bitbucket.org")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateTimeAdapter()).build()))
            .build()

        return retrofit.create(BitbucketService::class.java)
    }
}
