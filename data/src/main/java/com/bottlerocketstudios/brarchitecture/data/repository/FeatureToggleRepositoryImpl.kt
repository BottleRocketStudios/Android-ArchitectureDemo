package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.converter.toFeatureToggle
import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleDto
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent

@Suppress("TooManyFunctions")
class FeatureToggleRepositoryImpl(private val moshi: Moshi) : FeatureToggleRepository, KoinComponent {
    // DI
    private val _featureToggles = MutableStateFlow<Set<FeatureToggle>>(emptySet())
    override val featureToggles: StateFlow<Set<FeatureToggle>> = _featureToggles

    init {
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
