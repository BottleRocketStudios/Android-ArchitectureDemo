package com.bottlerocketstudios.brarchitecture.ui.featuretoggle

import androidx.compose.runtime.Composable
import com.bottlerocketstudios.compose.featuretoggles.FeatureToggleState

@Composable
fun FeatureToggleViewModel.toState() = FeatureToggleState(
    featureToggles = featureToggles,
    onResetFeatureTogglesClick = { onResetFeatureToggleClick() },
    updateFeatureToggleValue = { toggle -> updateFeatureToggleValue(toggle) }
)
