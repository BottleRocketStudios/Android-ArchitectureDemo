package com.bottlerocketstudios.brarchitecture.domain.repositories

import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
interface FeatureToggleRepository : com.bottlerocketstudios.brarchitecture.domain.models.Repository {
    val featureToggles: Flow<List<FeatureToggle>>
    fun getFeatureTogglesFromJson()
    fun getFeatureToggle(name: String): Boolean
}
