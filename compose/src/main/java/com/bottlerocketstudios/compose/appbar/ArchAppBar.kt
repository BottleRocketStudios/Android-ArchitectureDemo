package com.bottlerocketstudios.compose.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.bottlerocketstudios.compose.widgets.AppBar
import kotlinx.coroutines.launch

@Composable
fun ArchAppBar(
    state: ArchAppBarState,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    navIntercept: (() -> Boolean)?,
) {
    AnimatedVisibility(
        visible = state.showToolbar.value,
        enter = slideInVertically(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ),
        exit = slideOutVertically(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        )
    ) {
        val coroutineScope = rememberCoroutineScope()
        val topLevel = state.topLevel

        AppBar(
            title = state.title.value,
            navIcon = if (topLevel.value) Icons.Default.Menu else Icons.Default.ArrowBack,
            onNavClicked = {
                // If user is at top of navigation, toggle side drawer
                if (topLevel.value) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                } else {
                    // Then check nav intercept. Otherwise navigate upwards.
                    if (navIntercept?.invoke() != true) {
                        navController.popBackStack()
                    }
                }
            }
        )
    }
}
