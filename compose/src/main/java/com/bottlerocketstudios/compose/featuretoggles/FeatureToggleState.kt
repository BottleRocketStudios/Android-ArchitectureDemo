package com.bottlerocketstudios.compose.featuretoggles

import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import kotlinx.coroutines.flow.StateFlow

class FeatureToggleState(
    var featureToggles: StateFlow<Set<FeatureToggle>>,
    val onResetFeatureTogglesClick: () -> Unit,
    val updateFeatureToggleValue: (FeatureToggle.ToggleValueBoolean) -> Unit,
)
