package com.bottlerocketstudios.brarchitecture.data.network.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bitbucket_credentials_v2")

@Suppress("Deprecation")
internal class BitbucketCredentialsRepository(private val context: Context, private val json: Json) : Repository {

    companion object {
        // Keys for DataStore
        private val CREDENTIALS_KEY = stringPreferencesKey("BitbucketCredentials")
        private val TOKEN_KEY = stringPreferencesKey("BitbucketToken")

        // Legacy constants for Migration
        private const val OLD_PREF_FILE_NAME = "secureBbCredentials"
        private const val OLD_CREDENTIALS_KEY = "BitbucketCredentials"
        private const val OLD_TOKEN_KEY = "BitbucketToken"
    }

    // Initialize Tink Aead
    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "master_key_preference")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://tink_master_key")
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    // Old EncryptedSharedPreferences for migration
    private val oldPrefs: SharedPreferences by lazy {
        val masterKey = androidx.security.crypto.MasterKey.Builder(context)
            .setKeyScheme(androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            OLD_PREF_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    suspend fun clearStorage() {
        context.dataStore.edit { it.clear() }
        // Also clear legacy if exists, just in case
        if (legacyFileExists()) {
            oldPrefs.edit().clear().apply()
        }
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

        // Migration Check
        if (legacyFileExists() && oldPrefs.contains(OLD_CREDENTIALS_KEY)) {
            val oldJson = oldPrefs.getString(OLD_CREDENTIALS_KEY, null)
            if (!oldJson.isNullOrEmpty()) {
                Timber.i("Migrating credentials from EncryptedSharedPreferences")
                storeCredentials(json.decodeFromString(ValidCredentialSerializer, oldJson))
                oldPrefs.edit().remove(OLD_CREDENTIALS_KEY).apply()
                return json.decodeFromString(ValidCredentialSerializer, oldJson)
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

        // Migration Check
        if (legacyFileExists() && oldPrefs.contains(OLD_TOKEN_KEY)) {
            val oldJson = oldPrefs.getString(OLD_TOKEN_KEY, null)
            if (!oldJson.isNullOrEmpty()) {
                Timber.i("Migrating token from EncryptedSharedPreferences")
                storeToken(json.decodeFromString(oldJson))
                oldPrefs.edit().remove(OLD_TOKEN_KEY).apply()
                return json.decodeFromString(oldJson)
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

    private fun legacyFileExists(): Boolean {
        // Quick check if the shared prefs file exists
        val file = java.io.File(context.filesDir.parent, "shared_prefs/$OLD_PREF_FILE_NAME.xml")
        return file.exists()
    }
}
