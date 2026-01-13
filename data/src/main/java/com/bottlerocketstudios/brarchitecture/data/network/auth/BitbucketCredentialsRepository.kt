package com.bottlerocketstudios.brarchitecture.data.network.auth

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.AccessToken
import com.bottlerocketstudios.brarchitecture.data.serialization.ValidCredentialSerializer
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.nio.charset.StandardCharsets

private const val DATASTORE_NAME = "bitbucket_credentials"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

internal class BitbucketCredentialsRepository(private val context: Context, private val json: Json) : Repository {

    companion object {
        // Keys for DataStore
        private val CREDENTIALS_KEY = stringPreferencesKey("BitbucketCredentials")
        private val TOKEN_KEY = stringPreferencesKey("BitbucketToken")

        // Tink Configuration
        private const val TINK_KEYSET_NAME = "tink_keyset"
        private const val MASTER_KEY_PREFERENCE = "master_key_preference"
        private const val MASTER_KEY_URI = "android-keystore://tink_master_key"
        private const val AES_MODE = "AES256_GCM"
    }

    // Initialize Tink Aead
    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, TINK_KEYSET_NAME, MASTER_KEY_PREFERENCE)
            .withKeyTemplate(KeyTemplates.get(AES_MODE))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    suspend fun clearStorage() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun storeCredentials(credentials: ValidCredentialModel) {
        val jsonString = json.encodeToString(ValidCredentialSerializer, credentials)
        val encrypted = encrypt(jsonString)
        context.dataStore.edit { it[CREDENTIALS_KEY] = encrypted }
    }

    suspend fun loadCredentials(): ValidCredentialModel? {
        val prefs = context.dataStore.data.first()
        val encryptedJson = prefs[CREDENTIALS_KEY]

        if (encryptedJson != null) {
            return try {
                val jsonString = decrypt(encryptedJson)
                json.decodeFromString(ValidCredentialSerializer, jsonString)
            } catch (e: Exception) {
                Timber.e(e, "Failed to decrypt/decode credentials")
                null
            }
        }

        return null
    }

    suspend fun storeToken(token: AccessToken) {
        val jsonString = json.encodeToString(token)
        val encrypted = encrypt(jsonString)
        context.dataStore.edit { it[TOKEN_KEY] = encrypted }
    }

    suspend fun loadToken(): AccessToken? {
        val prefs = context.dataStore.data.first()
        val encryptedJson = prefs[TOKEN_KEY]

        if (encryptedJson != null) {
            return try {
                val jsonString = decrypt(encryptedJson)
                json.decodeFromString<AccessToken>(jsonString)
            } catch (e: Exception) {
                Timber.e(e, "Failed to decrypt/decode token")
                null
            }
        }

        return null
    }

    private fun encrypt(plainText: String): String {
        val bytes = plainText.toByteArray(StandardCharsets.UTF_8)
        val encryptedBytes = aead.encrypt(bytes, null)
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    private fun decrypt(cipherText: String): String {
        val bytes = Base64.decode(cipherText, Base64.NO_WRAP)
        val decryptedBytes = aead.decrypt(bytes, null)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }
}
