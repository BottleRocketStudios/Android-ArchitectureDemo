package com.bottlerocketstudios.brarchitecture.data.environment

import android.content.SharedPreferences
import androidx.core.content.edit
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentType
import timber.log.Timber

class EnvironmentRepositoryImpl(private val sharedPrefs: SharedPreferences, private val buildConfigProvider: BuildConfigProvider) : EnvironmentRepository {

    @Suppress("PrivatePropertyName", "VariableNaming") // constant
    private val ENVIRONMENT_STG = EnvironmentConfig(EnvironmentType.STG, STG_BASE_URL)
    @Suppress("PrivatePropertyName", "VariableNaming") // constant
    private val ENVIRONMENT_PROD = EnvironmentConfig(EnvironmentType.PRODUCTION, PROD_BASE_URL)
    @Suppress("PrivatePropertyName", "VariableNaming") // constant
    /** Represents environment to use as a fallback in case any issues (ex: sharedPref loading issue, selected environment no longer in environment list) */
    private val FALLBACK_ENVIRONMENT = ENVIRONMENT_STG

    override val environments: List<EnvironmentConfig> = if (buildConfigProvider.isProductionReleaseBuild) listOf(ENVIRONMENT_PROD) else listOf(ENVIRONMENT_STG, ENVIRONMENT_PROD)

    override val selectedConfig: EnvironmentConfig
        get() = if (buildConfigProvider.isProductionReleaseBuild) {
            ENVIRONMENT_PROD
        } else {
            val selectedEnvironmentType = sharedPrefs.getString(KEY_SELECTED_ENVIRONMENT_TYPE, FALLBACK_ENVIRONMENT.environmentType.asEnvironmentPrefValue()).orEmpty().asEnvironmentType()
            environments.firstOrNull { it.environmentType == selectedEnvironmentType } ?: FALLBACK_ENVIRONMENT
        }

    override fun changeEnvironment(environmentType: EnvironmentType) {
        if (buildConfigProvider.isProductionReleaseBuild) {
            // no-op
            return
        }
        Timber.v("[changeEnvironment] new environmentType=$environmentType")
        sharedPrefs.edit {
            putString(KEY_SELECTED_ENVIRONMENT_TYPE, environmentType.asEnvironmentPrefValue())
        }
    }

    private fun EnvironmentType.asEnvironmentPrefValue(): String {
        return when (this) {
            EnvironmentType.STG -> SP_ENVIRONMENT_VALUE_STG
            EnvironmentType.PRODUCTION -> SP_ENVIRONMENT_VALUE_PRODUCTION
        }
    }

    private fun String?.asEnvironmentType(): EnvironmentType? {
        return when (this) {
            SP_ENVIRONMENT_VALUE_STG -> EnvironmentType.STG
            SP_ENVIRONMENT_VALUE_PRODUCTION -> EnvironmentType.PRODUCTION
            else -> null
        }
    }

    companion object {
        private const val KEY_SELECTED_ENVIRONMENT_TYPE = "selected_environment_type"
        private const val SP_ENVIRONMENT_VALUE_STG = "env_stage"
        private const val SP_ENVIRONMENT_VALUE_PRODUCTION = "env_production"
    }
}

// FIXME: Replace with proper stage/production base urls!
// Base Urls per environment
private const val STG_BASE_URL = "https://REPLACE-THIS-HARDCODED-URL.STAGE.CLIENT.com"
private const val PROD_BASE_URL = "https://REPLACE-THIS-HARDCODED-URL.PROD.CLIENT.com"
