package com.bottlerocketstudios.brarchitecture.domain.repositories

import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import kotlinx.coroutines.flow.StateFlow

@Suppress("TooManyFunctions")
interface FeatureToggleRepository : com.bottlerocketstudios.brarchitecture.domain.models.Repository {
    /** Flow of all feature toggles */
    val featureToggles: StateFlow<Set<FeatureToggle>>

    /** Persists a local override for the given feature toggle using the included [FeatureToggle] to disk. */
    fun overrideFeatureToggleValue(toggleWithUpdateValue: FeatureToggle)

    /** Resets [featureToggles] back to the default value per toggle and potentially triggers an update to [featureToggles] if there are changes. */
    fun resetTogglesToDefaultValues()
}
