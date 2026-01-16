package com.bottlerocketstudios.brarchitecture.ui.featuretoggle

import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import org.koin.core.component.inject
import timber.log.Timber

class FeatureToggleViewModel : BaseViewModel() {
    // region DI
    private val featureToggleRepository: FeatureToggleRepository by inject()
    // endregion

    // region UI State
    var featureToggles = featureToggleRepository.featureToggles
    // endregion

    // region UI Callbacks
    fun onResetFeatureToggleClick() {
        Timber.v("[onResetFeatureToggleClick] Resetting Toggles now...")
        featureToggleRepository.resetTogglesToDefaultValues()
    }

    fun updateFeatureToggleValue(featureToggle: FeatureToggle) {
        featureToggleRepository.updateFeatureToggleValue(featureToggle)
    }
    // endregion
}
