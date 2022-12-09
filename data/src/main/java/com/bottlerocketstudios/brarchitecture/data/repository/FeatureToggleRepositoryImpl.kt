package com.bottlerocketstudios.brarchitecture.data.repository

import com.bottlerocketstudios.brarchitecture.data.converter.toFeatureToggle
import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleDto
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent

@Suppress("TooManyFunctions")
class FeatureToggleRepositoryImpl(private val moshi: Moshi) : FeatureToggleRepository, KoinComponent {
    // DI
    private val _featureToggles = MutableStateFlow<List<FeatureToggleDto>>(emptyList())
    override val featureToggles: Flow<List<FeatureToggle>> = _featureToggles.map { list -> list.map { it.toFeatureToggle() } }

    init {
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
