package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import android.content.Context
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.vault.SharedPreferenceVault
import com.bottlerocketstudios.vault.SharedPreferenceVaultFactory
import com.bottlerocketstudios.vault.SharedPreferenceVaultRegistry
import com.bottlerocketstudios.vault.keys.generator.Aes256RandomKeyFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber.e
import java.security.GeneralSecurityException

class BitbucketCredentialsRepository(private val context: Context) {

    private var vault: SharedPreferenceVault? = null

    companion object {
        private const val PREF_FILE_NAME = "bbCredentials"
        private const val KEY_FILE_NAME = "bbCredentialsKey"
        private const val KEY_ALIAS = "bbCredentialsKeyAlias"
        private const val VAULT_ID = 0xdeadbeef

        private const val PRESHARED_SECRET = "s5T13J0F07Hk6AvWK3hWC4qt8WAzlsrnQVBqAuQ3"
        private const val BITBUCKET_CREDENTIALS = "BitbucketCredentials"
        private const val BITBUCKET_TOKEN = "BitbucketCredentials"
        private val CREDENTIAL_ADAPTER = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(ValidCredentialModel::class.java)
        private val TOKEN_ADAPTER = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(TokenAuthRepository.AccessToken::class.java)
    }

    fun clearStorage() {
        vault?.let {
            it.clearStorage()

            Thread(Runnable {
                getSharedPreferenceVault()?.let {
                    val secretKey = Aes256RandomKeyFactory.createKey()
                    it.rekeyStorage(secretKey)
                }
            }).start()
        }
    }

    private fun getSharedPreferenceVault(): SharedPreferenceVault? {
        if (vault == null) { vault = initVault(context.applicationContext) }
        if (vault == null) { e("Could not initialize Vault") }
        return vault
    }

    private fun initVault(applicationContext: Context): SharedPreferenceVault? {
        var secureVault: SharedPreferenceVault? = null

        try {
            secureVault = SharedPreferenceVaultFactory.getAppKeyedCompatAes256Vault(
                    applicationContext,
                    PREF_FILE_NAME,
                    KEY_FILE_NAME,
                    KEY_ALIAS,
                    VAULT_ID.toInt(),
                    PRESHARED_SECRET
            )

            SharedPreferenceVaultRegistry.getInstance().addVault(
                    VAULT_ID.toInt(),
                    PREF_FILE_NAME,
                    KEY_ALIAS,
                    secureVault
            )
        } catch (e: GeneralSecurityException) {
            e(e, "Caught java.security.GeneralSecurityException")
        }

        return secureVault
    }

    fun storeCredentials(credentials: ValidCredentialModel): Boolean {
        getSharedPreferenceVault()?.let {
            it.edit().putString(BITBUCKET_CREDENTIALS, CREDENTIAL_ADAPTER.toJson(credentials)).apply()
            return true
        }

        return false
    }

    fun loadCredentials(): ValidCredentialModel? {
        val credentialsJson = getSharedPreferenceVault()?.getString(BITBUCKET_CREDENTIALS, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            CREDENTIAL_ADAPTER.fromJson(credentialsJson)
        } else {
            e("Credentials repository could not load credentials")
            null
        }
    }

    fun storeToken(token: TokenAuthRepository.AccessToken): Boolean {
        getSharedPreferenceVault()?.let {
            it.edit().putString(BITBUCKET_TOKEN, TOKEN_ADAPTER.toJson(token)).apply()
            return true
        }

        return false
    }

    fun loadToken(): TokenAuthRepository.AccessToken? {
        val credentialsJson = getSharedPreferenceVault()?.getString(BITBUCKET_TOKEN, null)
        return if (!credentialsJson.isNullOrEmpty()) {
            TOKEN_ADAPTER.fromJson(credentialsJson)
        } else {
            e("Credentials repository could not load credentials")
            null
        }
    }
}
