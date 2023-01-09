package com.bottlerocketstudios.brarchitecture.ui.devoptions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.devoptions.DevOptionsState

@Composable
fun DevOptionsViewModel.toState() = DevOptionsState(
    environmentNames = environmentNames.collectAsState(),
    environmentSpinnerPosition = environmentSpinnerPosition.collectAsState(),
    baseUrl = baseUrl.collectAsState(),
    appVersionName = applicationInfo.appVersionName,
    appVersionCode = applicationInfo.appVersionCode,
    appId = applicationInfo.appId,
    buildIdentifier = applicationInfo.buildIdentifier,
    onEnvironmentChanged = { index -> onEnvironmentChanged(index) },
    onRestartCtaClick = { onRestartCtaClick() },
    onForceCrashCtaClicked = { onForceCrashCtaClicked() },
    onFeatureToggleCtaClicked = { onFeatureToggleCtaClicked() }
)
