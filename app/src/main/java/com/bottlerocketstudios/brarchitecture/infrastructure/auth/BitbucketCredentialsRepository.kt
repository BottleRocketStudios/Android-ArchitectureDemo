package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.squareup.moshi.Moshi
import timber.log.Timber.e

class BitbucketCredentialsRepository(context: Context) {
    companion object {
        private const val SECURE_PREF_FILE_NAME = "secureBbCredentials"
        private const val BITBUCKET_CREDENTIALS = "BitbucketCredentials"
        private const val BITBUCKET_TOKEN = "BitbucketToken"
        private val CREDENTIAL_ADAPTER = Moshi.Builder()
            .build()
            .adapter(ValidCredentialModel::class.java)
        private val TOKEN_ADAPTER = Moshi.Builder()
            .build()
            .adapter(TokenAuthRepository.AccessToken::class.java)
    }

    private val encryptedSharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        SECURE_PREF_FILE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context.applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun clearStorage() {
        encryptedSharedPrefs.edit().clear().apply()
    }

    fun storeCredentials(credentials: ValidCredentialModel) {
        encryptedSharedPrefs.edit().putString(BITBUCKET_CREDENTIALS, CREDENTIAL_ADAPTER.toJson(credentials)).apply()
    }

    fun loadCredentials(): ValidCredentialModel? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_CREDENTIALS, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            CREDENTIAL_ADAPTER.fromJson(credentialsJson)
        } else {
            e("Credentials repository could not load credentials")
            null
        }
    }

    fun storeToken(token: TokenAuthRepository.AccessToken) {
        encryptedSharedPrefs.edit().putString(BITBUCKET_TOKEN, TOKEN_ADAPTER.toJson(token)).apply()
    }

    fun loadToken(): TokenAuthRepository.AccessToken? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_TOKEN, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            TOKEN_ADAPTER.fromJson(credentialsJson)
        } else {
            e("Credentials repository could not load credentials")
            null
        }
    }
}
