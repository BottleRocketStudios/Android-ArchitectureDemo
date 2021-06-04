package com.bottlerocketstudios.brarchitecture.data.network.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.repository.Repository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import timber.log.Timber.e

internal class BitbucketCredentialsRepository(context: Context, private val moshi: Moshi) : Repository {
    companion object {
        private const val SECURE_PREF_FILE_NAME = "secureBbCredentials"
        private const val BITBUCKET_CREDENTIALS = "BitbucketCredentials"
        private const val BITBUCKET_TOKEN = "BitbucketToken"
    }

    private val CREDENTIAL_ADAPTER: JsonAdapter<ValidCredentialModel> by lazy { moshi.adapter(ValidCredentialModel::class.java) }
    private val TOKEN_ADAPTER: JsonAdapter<AccessToken> by lazy { moshi.adapter(AccessToken::class.java) }
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

    fun storeToken(token: AccessToken) {
        encryptedSharedPrefs.edit().putString(BITBUCKET_TOKEN, TOKEN_ADAPTER.toJson(token)).apply()
    }

    fun loadToken(): AccessToken? {
        val credentialsJson = encryptedSharedPrefs.getString(BITBUCKET_TOKEN, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            TOKEN_ADAPTER.fromJson(credentialsJson)
        } else {
            e("Credentials repository could not load credentials")
            null
        }
    }
}
