package com.bottlerocketstudios.brarchitecture.domain.repositories

import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
interface FeatureToggleRepository : com.bottlerocketstudios.brarchitecture.domain.models.Repository {
    val featureToggles: Flow<List<FeatureToggle>>
    val featureTogglesByRemoteConfig: Flow<Map<String, Boolean>>
    fun getFeatureTogglesFromJson()
    fun getFeatureToggle(name: String): Boolean
    fun initRemoteConfigSettings()
    fun getFeatureToggleFromConfig(name: String): Boolean
}
