package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.network.CognitoService
import com.bottlerocketstudios.brarchitecture.domain.models.CognitoUser
import com.bottlerocketstudios.brarchitecture.domain.repositories.CognitoRepository
import kotlinx.coroutines.flow.MutableStateFlow

import timber.log.Timber

class CognitoRepositoryImpl(
    private val cognitoService: CognitoService
) : CognitoRepository {

    // In a real app we would persist these tokens securely
    private val _accessToken = MutableStateFlow<String?>(null)

    override suspend fun authenticate(authCode: String): Boolean {
        return try {
            val response = cognitoService.getToken(authCode)
            _accessToken.value = response.accessToken
            Timber.d("Cognito authentication successful")
            true
        } catch (e: Exception) {
            Timber.e(e, "Cognito authentication failed")
            false
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return _accessToken.value != null
    }

    override fun clear() {
        _accessToken.value = null
    }

    override suspend fun getUser(): CognitoUser? {
        val token = _accessToken.value
        return if (token != null) {
            try {
                val dto = cognitoService.getUser(token)
                CognitoUser(
                    sub = dto.sub,
                    email = dto.email,
                    username = dto.username,
                    name = dto.name
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to get Cognito user")
                null
            }
        } else {
            null
        }
    }
}
