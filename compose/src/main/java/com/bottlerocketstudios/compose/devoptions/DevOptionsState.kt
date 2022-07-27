package com.bottlerocketstudios.compose.devoptions

import androidx.compose.runtime.State

data class DevOptionsState(
    val environmentNames: State<List<String>>,
    val environmentSpinnerPosition: State<Int>,
    val baseUrl: State<String>,
    val appVersionName: String,
    val appVersionCode: String,
    val appId: String,
    val buildIdentifier: String,
    val onEnvironmentChanged: (Int) -> Unit,
    val onRestartCtaClick: () -> Unit,
    val onForceCrashCtaClicked: () -> Unit
)
