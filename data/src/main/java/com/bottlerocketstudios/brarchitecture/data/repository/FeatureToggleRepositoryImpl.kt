package com.bottlerocketstudios.brarchitecture.data.repository

import android.app.Application
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("TooManyFunctions")
class FeatureToggleRepositoryImpl(private val moshi: Moshi) : FeatureToggleRepository, KoinComponent {
    // DI
    private val app: Application by inject()

    private val _featureToggles = MutableStateFlow<List<FeatureToggleDto>>(emptyList())
    override val featureToggles: Flow<List<FeatureToggle>> = _featureToggles.map { list -> list.map { it.toFeatureToggle() } }

    init {
        getFeatureTogglesFromJson()
    }

    override fun getFeatureTogglesFromJson() {
        val listType = Types.newParameterizedType(List::class.java, FeatureToggleDto::class.java)
        val adapter: JsonAdapter<List<FeatureToggleDto>> = moshi.adapter(listType)
        val fileJson: String = app.assets.open("featureToggles.json").bufferedReader().use { it.readText() }
        _featureToggles.value = adapter.fromJson(fileJson) ?: emptyList()
    }

    override fun getFeatureToggle(name: String): Boolean {
        return _featureToggles.value.find { it.name == name }?.value == true
    }
}
