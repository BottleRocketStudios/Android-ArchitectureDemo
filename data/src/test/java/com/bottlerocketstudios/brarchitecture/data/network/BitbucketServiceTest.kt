package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.model.toProtectedProperty
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import com.bottlerocketstudios.brarchitecture.data.serialization.DateTimeAdapter
import com.bottlerocketstudios.brarchitecture.data.serialization.ProtectedPropertyAdapter
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
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
            accessToken = "REPLACE WITH REAL TOKEN".toProtectedProperty(),
            refreshToken = "REPLACE WITH REAL TOKEN".toProtectedProperty(),
            expiresInSeconds = 7200,
            tokenType = "bearer",
            scopes = "project account pullrequest"
        )
        runTest {
            val bitbucketService = createBitbucketService(accessToken)
            val response = bitbucketService.getRepository("REPLACE WITH USERNAME", "REPLACE WITH PRIVATE REPOSITORY")
            val body = response.body()
            assertThat(body).isNotNull()
            body?.let { privateRepo: GitRepositoryDto ->
                assertThat(privateRepo.isPrivate).isNotNull()
                assertThat(privateRepo.isPrivate).isTrue()
            }
        }
    }

    @Test
    @Ignore("Test only works if actual expired tokens are provided")
    fun getRepository_shouldFail_whenForceTokenExpired() {
        val accessToken = AccessToken(
            accessToken = "REPLACE WITH REAL TOKEN".toProtectedProperty(),
            refreshToken = "REPLACE WITH REAL TOKEN".toProtectedProperty(),
            expiresInSeconds = 7200,
            tokenType = "bearer",
            scopes = "project account pullrequest"
        )
        runTest {
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
    fun getUser_shouldReturnUser_whenAuthenticated() = runTest {
        val bitbucketService = createBitbucketService(null)
        val response = bitbucketService.getUser()
        val body = response.body()
        val errorBody = response.errorBody()
    }

    // TODO: Consider adding koin-test and use a test koin graph instead of the manual creation here
    private suspend fun createBitbucketService(accessToken: AccessToken?): BitbucketService {
        val bitbucketCredentialsRepository = mock<BitbucketCredentialsRepository> {
            on { loadCredentials() } doReturn ValidCredentialModel("username".toProtectedProperty(), "password".toProtectedProperty())
            on { loadToken() } doReturn accessToken
        }

        val unauthOkHttpClient = OkHttpClient.Builder().build()
        val unathRetrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .client(unauthOkHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateTimeAdapter(mock())).add(ProtectedPropertyAdapter()).build()))
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
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateTimeAdapter(mock())).add(ProtectedPropertyAdapter()).build()))
            .build()

        return retrofit.create(BitbucketService::class.java)
    }
}
