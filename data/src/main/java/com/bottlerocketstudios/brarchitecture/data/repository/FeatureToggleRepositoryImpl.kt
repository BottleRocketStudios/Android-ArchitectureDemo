package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.R
import com.bottlerocketstudios.brarchitecture.data.converter.toFeatureToggle
import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleDto
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@Suppress("TooManyFunctions")
class FeatureToggleRepositoryImpl(private val moshi: Moshi) : FeatureToggleRepository, KoinComponent {
    // DI
    private val _featureToggles = MutableStateFlow<List<FeatureToggleDto>>(emptyList())
    override val featureToggles: Flow<List<FeatureToggle>> = _featureToggles.map { list -> list.map { it.toFeatureToggle() } }

    private val _featureTogglesByConfig = MutableStateFlow<Map<String, Boolean>>(mapOf())
    override val featureTogglesByRemoteConfig: Flow<Map<String, Boolean>> = _featureTogglesByConfig
    private val remoteConfig by inject<FirebaseRemoteConfig>()

    init {
        initRemoteConfigSettings()
        getFeatureTogglesFromJson()
    }

    override fun getFeatureTogglesFromJson() {
        val listType = Types.newParameterizedType(List::class.java, FeatureToggleDto::class.java)
        val adapter: JsonAdapter<List<FeatureToggleDto>> = moshi.adapter(listType)
        _featureToggles.value = adapter.fromJson(FEATURE_TOGGLE_JSON) ?: emptyList()
    }

    override fun getFeatureToggle(name: String): Boolean {
        return _featureToggles.value.find { it.name == name }?.value == true
    }

    override fun initRemoteConfigSettings() {
        remoteConfig.run {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    // interval used for dev testing, in PROD it is preferable to use higher intervals like 12hrs (43200s)
                    minimumFetchIntervalInSeconds = 60
                    build()
                })
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Timber.d("Config params updated: $updated.")
                        _featureTogglesByConfig.value = all.mapValues { it.value.asBoolean() }
                    }
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
        }
    }

    override fun getFeatureToggleFromConfig(name: String): Boolean {
        return _featureTogglesByConfig.value[name] == true
    }
}

@Language("JSON")
private const val FEATURE_TOGGLE_JSON =
    """
        [
            {
              "name": "SHOW_SNIPPETS",
              "value": false,
              "defaultValue": true,
              "requireRestart": false
            },
            {
              "name": "SHOW_PULL_REQUESTS",
              "value": false,
              "defaultValue": true,
              "requireRestart": false
            }
        ]
    """
