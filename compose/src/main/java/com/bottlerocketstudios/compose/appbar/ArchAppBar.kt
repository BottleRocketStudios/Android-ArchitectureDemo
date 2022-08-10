package com.bottlerocketstudios.compose.appbar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.bottlerocketstudios.launchpad.compose.widgets.slidingappbar.SlidingAppBar
import kotlinx.coroutines.launch

@Composable
fun ArchAppBar(
    state: ArchAppBarState,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    navIntercept: (() -> Boolean)?,
) {
    val coroutineScope = rememberCoroutineScope()
    val topLevel = state.topLevel
    val navIcon = if (topLevel.value) Icons.Default.Menu else Icons.Default.ArrowBack

    SlidingAppBar(
        visible = state.showToolbar.value,
        title = {
            Text(
                text = state.title.value,
                style = MaterialTheme.typography.h1
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
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
            ) {
                Icon(imageVector = navIcon, contentDescription = navIcon.name.split(".").firstOrNull())
            }
        },
    )
}
