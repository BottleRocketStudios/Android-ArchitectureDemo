package com.bottlerocketstudios.brarchitecture.data.network.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.serialization.ValidCredentialSerializer
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import kotlinx.serialization.json.Json
import timber.log.Timber

@Suppress("Deprecation")
internal class BitbucketCredentialsRepository(context: Context, private val json: Json) :
        Repository {
    companion object {
        private const val SECURE_PREF_FILE_NAME = "secureBbCredentials"
        private const val BITBUCKET_CREDENTIALS = "BitbucketCredentials"
        private const val BITBUCKET_TOKEN = "BitbucketToken"
    }

    private val masterKey =
            androidx.security.crypto.MasterKey.Builder(context)
                    .setKeyScheme(androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM)
                    .build()

    private val encryptedSharedPrefs: SharedPreferences =
            EncryptedSharedPreferences.create(
                    context,
                    SECURE_PREF_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

    fun clearStorage() {
        encryptedSharedPrefs.edit().clear().apply()
    }

    fun storeCredentials(credentials: ValidCredentialModel) {
        val jsonString = json.encodeToString(ValidCredentialSerializer, credentials)
        Timber.v("storeCredentials: encoding to %s", jsonString)
        encryptedSharedPrefs.edit().putString(BITBUCKET_CREDENTIALS, jsonString).apply()
    }

    fun loadCredentials(): ValidCredentialModel? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_CREDENTIALS, null)
        Timber.v("loadCredentials: raw JSON from prefs: %s", credentialsJson)
        return if (!credentialsJson.isNullOrEmpty()) {
            try {
                json.decodeFromString(ValidCredentialSerializer, credentialsJson)
            } catch (e: Exception) {
                Timber.e(
                        e,
                        "Credentials repository could not decode credentials. JSON: %s",
                        credentialsJson
                )
                null
            }
        } else {
            Timber.w("Credentials repository could not load credentials - key not found or empty")
            null
        }
    }

    fun storeToken(token: AccessToken) {
        val jsonString = json.encodeToString(token)
        Timber.v("storeToken: encoding to %s", jsonString)
        encryptedSharedPrefs.edit().putString(BITBUCKET_TOKEN, jsonString).apply()
    }

    fun loadToken(): AccessToken? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_TOKEN, null)
        Timber.v("loadToken: raw JSON from prefs: %s", credentialsJson)
        return if (!credentialsJson.isNullOrEmpty()) {
            try {
                json.decodeFromString(credentialsJson)
            } catch (e: Exception) {
                Timber.e(
                        e,
                        "Credentials repository could not decode token. JSON: %s",
                        credentialsJson
                )
                null
            }
        } else {
            Timber.w("Credentials repository could not load token - key not found or empty")
            null
        }
    }
}
