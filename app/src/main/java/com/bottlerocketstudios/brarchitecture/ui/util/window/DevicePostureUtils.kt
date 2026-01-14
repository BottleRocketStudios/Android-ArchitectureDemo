package com.bottlerocketstudios.brarchitecture.ui.util.window

import androidx.activity.ComponentActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun ComponentActivity.createDevicePostureFlow() =
        WindowInfoTracker.getOrCreate(this)
                .windowLayoutInfo(this)
                .flowWithLifecycle(this.lifecycle)
                .map { layoutInfo ->
                    val foldingFeature =
                            layoutInfo
                                    .displayFeatures
                                    .filterIsInstance<FoldingFeature>()
                                    .firstOrNull()
                    when {
                        isBookPosture(foldingFeature) -> {
                            DevicePosture.BookPosture(foldingFeature.bounds)
                        }

                        isSeparating(foldingFeature) -> {
                            DevicePosture.Separating(
                                        foldingFeature.bounds,
                                        foldingFeature.orientation
                                )
                        }

                        else -> {
                            DevicePosture.NormalPosture
                        }
                    }
                }
                .stateIn(
                        scope = lifecycleScope,
                        started = SharingStarted.Eagerly,
                        initialValue = DevicePosture.NormalPosture,
                )
