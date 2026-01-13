package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bottlerocketstudios.brarchitecture.ui.util.logger.TAG_NAV
import com.bottlerocketstudios.brarchitecture.ui.util.nav.NavigationType
import com.bottlerocketstudios.brarchitecture.ui.util.nav.Navigator
import com.bottlerocketstudios.brarchitecture.ui.util.nav.rememberNavigator
import com.bottlerocketstudios.brarchitecture.ui.util.nav.toNavigationType
import com.bottlerocketstudios.brarchitecture.ui.util.window.DevicePosture
import org.koin.core.component.getScopeName
import timber.log.Timber

@Composable
fun NavigationWrapper(
        widthSize: WindowWidthSizeClass,
        devicePosture: DevicePosture,
        app: @Composable (navigator: Navigator, bottomBar: (@Composable () -> Unit)) -> Unit,
) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navigator = rememberNavigator(startRoute = Routes.Splash, shouldPrintDebugInfo = true)
        val currentRoute = navigator.backStack.lastOrNull() ?: Routes.Splash
        Timber.tag(TAG_NAV).d("widthSize: $widthSize, devicePosture: ${devicePosture.javaClass.simpleName}, Current Route: ${currentRoute.javaClass.simpleName}")

        // For now, we reuse the navigation logic from ComposeActivity but adapted for Navigation 3
        // In a real scenario, we might want to port NavigationRail etc. if they existed.
        // Here we will focus on the structure requested.

        val navigationType =
                remember(widthSize, devicePosture) {
                        derivedStateOf { widthSize.toNavigationType(devicePosture) }
                }

        when (navigationType.value) {
                NavigationType.PERMANENT_NAVIGATION_DRAWER ->
                        PermanentNavigationDrawer(
                                drawerContent = {
                                        PermanentDrawerSheet {
                                                // TODO: Implement Drawer Content if needed, or
                                                // placeholder
                                        }
                                },
                        ) { app(navigator) {} }
                NavigationType.MODAL_NAVIGATION ->
                        ModalNavigationDrawer(
                                drawerState = drawerState,
                                drawerContent = {
                                        ModalDrawerSheet {
                                                // TODO: Implement Drawer Content if needed
                                        }
                                },
                        ) { app(navigator) {} }
                else ->
                        Row {
                                AnimatedVisibility(
                                        visible =
                                                navigationType.value ==
                                                        NavigationType.NAVIGATION_RAIL,
                                        enter =
                                                slideInVertically(
                                                        animationSpec =
                                                                spring(
                                                                        stiffness =
                                                                                Spring.StiffnessHigh
                                                                )
                                                ),
                                        exit =
                                                slideOutHorizontally(
                                                        animationSpec =
                                                                spring(
                                                                        stiffness =
                                                                                Spring.StiffnessHigh
                                                                )
                                                ),
                                ) {
                                        // TODO: Implement Navigation Rail if needed
                                }

                                Column(
                                        modifier = Modifier.fillMaxSize(),
                                ) {
                                        app(navigator) {
                                                AnimatedVisibility(
                                                        visible =
                                                                navigationType.value ==
                                                                        NavigationType
                                                                                .BOTTOM_NAVIGATION,
                                                        enter =
                                                                slideInVertically(
                                                                        animationSpec =
                                                                                spring(
                                                                                        stiffness =
                                                                                                Spring.StiffnessHigh
                                                                                )
                                                                ),
                                                        exit =
                                                                slideOutHorizontally(
                                                                        animationSpec =
                                                                                spring(
                                                                                        stiffness =
                                                                                                Spring.StiffnessHigh
                                                                                )
                                                                ),
                                                ) {
                                                        // TODO: Implement Bottom Bar if needed
                                                }
                                        }
                                }
                        }
        }
}
