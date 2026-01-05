package com.bottlerocketstudios.brarchitecture.ui.util.nav

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.bottlerocketstudios.brarchitecture.ui.util.window.DevicePosture

enum class NavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER,
    MODAL_NAVIGATION,
}

fun WindowWidthSizeClass.toNavigationType(devicePosture: DevicePosture) =
        when (this) {
            WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION
            WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded ->
                    if (devicePosture is DevicePosture.BookPosture) NavigationType.NAVIGATION_RAIL
                    else NavigationType.PERMANENT_NAVIGATION_DRAWER
            else -> NavigationType.BOTTOM_NAVIGATION
        }
