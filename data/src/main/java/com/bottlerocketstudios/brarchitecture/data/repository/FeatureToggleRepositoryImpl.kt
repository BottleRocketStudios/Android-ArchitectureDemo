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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@Suppress("TooManyFunctions")
class FeatureToggleRepositoryImpl(private val moshi: Moshi) : FeatureToggleRepository, KoinComponent {

    private val _featureToggles = MutableStateFlow<Set<FeatureToggle>>(emptySet())
    override val featureToggles: StateFlow<Set<FeatureToggle>> = _featureToggles

    private val remoteConfig by inject<FirebaseRemoteConfig>()
    private val _featureTogglesByConfig = MutableStateFlow<Set<FeatureToggle>>(emptySet())
    override val featureTogglesByRemoteConfig: StateFlow<Set<FeatureToggle>> = _featureTogglesByConfig

    init {
        initRemoteConfigSettings()
        getFeatureTogglesFromJson()
    }

    private fun getFeatureTogglesFromJson() {
        _featureToggles.value = getAdaptedToggles()
    }

    private fun getAdaptedToggles(): Set<FeatureToggle> {
        val adapter: JsonAdapter<FeatureToggleDto> = moshi.adapter(FeatureToggleDto::class.java)
        return mutableSetOf<FeatureToggle>().apply {
            addAll(adapter.fromJson(FEATURE_TOGGLE_JSON)?.booleanFlags?.map { it.toFeatureToggle() }?.toSet() ?: emptySet())
            addAll(adapter.fromJson(FEATURE_TOGGLE_JSON)?.stringFlags?.map { it.toFeatureToggle() }?.toSet() ?: emptySet())
        }
    }

    override fun overrideFeatureToggleValue(toggleWithUpdateValue: FeatureToggle) {
        TODO("Not yet impl - use DataStore to accomplish local overrides.")
    }

    override fun resetTogglesToDefaultValues() {
        val adaptedToggles = getAdaptedToggles()
        adaptedToggles.map {
            // Using a when so that if we want to play with more feature toggles for the demo, we can just add to the cases
            when (it) {
                is FeatureToggle.ToggleValueBoolean -> {
                    it.value = it.defaultValue
                }
                else -> {
                    (it as FeatureToggle.ToggleValueEnum).value = it.defaultValue
                }
            }
        }
        _featureToggles.value = adaptedToggles
    }

    private fun initRemoteConfigSettings() {
        remoteConfig.run {
            setConfigSettingsAsync(remoteConfigSettings {
                // interval used for dev testing, in PROD it is preferable to use higher intervals like 12hrs (43200s)
                minimumFetchIntervalInSeconds = 60
                build()
            })
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Timber.d("Config params updated: $updated.")
                    val remoteConfigSet = mutableSetOf<FeatureToggle>().apply {
                        all.forEach {
                            if (it.key == "WEBVIEW_CONFIGURATION") {
                                val enumValue = FeatureToggle.ToggleValueEnum.ToggleEnum.valueOf(it.value.asString())
                                add(
                                    FeatureToggle.ToggleValueEnum(
                                        name = it.key, value = enumValue, defaultValue = FeatureToggle.ToggleValueEnum.ToggleEnum.EXTERNAL_BROWSER, requireRestart = false
                                    )
                                )
                            } else {
                                add(FeatureToggle.ToggleValueBoolean(name = it.key, value = it.value.asBoolean(), defaultValue = it.value.asBoolean(), requireRestart = false))
                            }
                        }
                    }
                    _featureTogglesByConfig.value = remoteConfigSet
                }
            }
        }.addOnFailureListener {
            Timber.e(it)
        }
    }

    override fun updateFeatureToggleValue(toggleWithUpdateValue: FeatureToggle) {
        val adaptedToggles = _featureToggles.value
        adaptedToggles.map {
            // Using a when so that if we want to play with more feature toggles for the demo, we can just add to the cases
            when (it) {
                is FeatureToggle.ToggleValueBoolean -> {
                    if (toggleWithUpdateValue is FeatureToggle.ToggleValueBoolean) {
                        if (it.name == toggleWithUpdateValue.name) {
                            it.value = toggleWithUpdateValue.value
                        }
                    }
                }
                is FeatureToggle.ToggleValueEnum -> {
                    if (toggleWithUpdateValue is FeatureToggle.ToggleValueEnum) {
                        if (it.name == toggleWithUpdateValue.name) {
                            it.value = toggleWithUpdateValue.value
                        }
                    }
                }
            }
        }
        _featureToggles.value = mutableSetOf<FeatureToggle>().apply { addAll(adaptedToggles) }
    }
}

@Language("JSON")
private const val FEATURE_TOGGLE_JSON =
    """{
        "booleanFlags" :
        [
            {
                "name": "SHOW_SNIPPETS",
                "value": true,
                "defaultValue": true,
                "requireRestart": false
            },
            {
                "name": "SHOW_PULL_REQUESTS",
                "value": true,
                "defaultValue": true,
                "requireRestart": false
            }
        ],
        "stringFlags" :
        [
            {
                "name": "WEBVIEW_CONFIGURATION",
                "value": "WEBVIEW",
                "defaultValue": "EXTERNAL_BROWSER",
                "requireRestart": false
            }

        ]
    }"""
