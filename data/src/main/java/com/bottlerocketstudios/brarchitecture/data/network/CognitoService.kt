package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.BuildConfig
import com.bottlerocketstudios.brarchitecture.data.model.CognitoUserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

// Define DTOs for Token Response
@Serializable
data class CognitoTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("id_token") val idToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int
)

class CognitoService : KoinComponent {
    private val client: HttpClient by inject(named("api"))

    suspend fun getToken(authCode: String): CognitoTokenResponse {
        val domain = BuildConfig.COGNITO_DOMAIN
        return client.submitForm(
            url = "https://$domain.auth.us-east-1.amazoncognito.com/oauth2/token",
            formParameters = Parameters.build {
                append("grant_type", "authorization_code")
                append("client_id", BuildConfig.COGNITO_CLIENT_ID)
                append("code", authCode)
                append("redirect_uri", "https://www.bottlerocketstudios.com")
            }
        ).body()
    }
    suspend fun getUser(authToken: String): CognitoUserDto {
        val domain = BuildConfig.COGNITO_DOMAIN
        val endpoint = "https://$domain.auth.us-east-1.amazoncognito.com/oauth2/userInfo"

        return client.get(endpoint) {
            headers {
                append("Authorization", "Bearer $authToken")
            }
        }.body()
    }
}


