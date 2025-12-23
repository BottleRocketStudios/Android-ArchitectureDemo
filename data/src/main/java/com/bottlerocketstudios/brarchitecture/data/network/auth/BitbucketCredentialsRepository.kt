package com.bottlerocketstudios.brarchitecture.data.network.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import com.bottlerocketstudios.brarchitecture.data.serialization.ValidCredentialSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

internal class BitbucketCredentialsRepository(context: Context, private val json: Json) : Repository {
    companion object {
        private const val SECURE_PREF_FILE_NAME = "secureBbCredentials"
        private const val BITBUCKET_CREDENTIALS = "BitbucketCredentials"
        private const val BITBUCKET_TOKEN = "BitbucketToken"
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
        encryptedSharedPrefs.edit().putString(BITBUCKET_CREDENTIALS, json.encodeToString(ValidCredentialSerializer, credentials)).apply()
    }

    fun loadCredentials(): ValidCredentialModel? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_CREDENTIALS, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            json.decodeFromString(ValidCredentialSerializer, credentialsJson)
        } else {
            Timber.e("Credentials repository could not load credentials")
            null
        }
    }

    fun storeToken(token: AccessToken) {
        encryptedSharedPrefs.edit().putString(BITBUCKET_TOKEN, json.encodeToString(token)).apply()
    }

    fun loadToken(): AccessToken? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_TOKEN, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            json.decodeFromString(credentialsJson)
        } else {
            Timber.e("Credentials repository could not load credentials")
            null
        }
    }
}
