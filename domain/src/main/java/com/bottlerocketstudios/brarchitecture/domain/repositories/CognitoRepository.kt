package com.bottlerocketstudios.brarchitecture.domain.repositories
import com.bottlerocketstudios.brarchitecture.domain.models.CognitoUser

import kotlinx.coroutines.flow.Flow

interface CognitoRepository {
    /**
     * Authenticates using the authorization code from Cognito.
     * @param authCode The authorization code returned from the hosted UI.
     * @return true if authentication was successful
     */
    suspend fun authenticate(authCode: String): Boolean
    suspend fun isAuthenticated(): Boolean
    fun clear()
    suspend fun getUser(): CognitoUser?
}
