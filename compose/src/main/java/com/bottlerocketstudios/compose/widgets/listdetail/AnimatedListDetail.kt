package com.bottlerocketstudios.compose.widgets.listdetail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bottlerocketstudios.compose.util.LaunchCollection
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

// Internal Graph Routes for use by this widget
object NavGraph {
    sealed class Route(val route: String) {
        object Detail : Route("detail/{selected}") {
            fun navigateRoute(selected: String?) = "detail/$selected"
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> AnimatedListDetail(
    list: List<T>,
    smallScreen: Boolean,
    keyProvider: (T) -> String = { it.toString() },
    scope: @Composable ListDetailScope<T>.() -> Unit
) {
    val navController = rememberAnimatedNavController()
    val listDetailScope = ListDetailScopeImpl(list).apply { scope() }

    AnimatedNavHost(navController = navController, startDestination = NavGraph.Route.Detail.route) {
        composable(
            route = NavGraph.Route.Detail.route
        ) { backStackEntry ->
            val selectedKey = backStackEntry.arguments?.getString("selected")
            val selected: T? = list.find { keyProvider(it) == selectedKey }
            listDetailScope.detailStateCallback(selected != null)

            // Use scoped selector to allow outside selection
            listDetailScope.selector.LaunchCollection { selectionKey ->
                navController.navigate(route = NavGraph.Route.Detail.navigateRoute(selectionKey)) {
                    popUpTo(NavGraph.Route.Detail.navigateRoute(null)) {
                        inclusive = true
                    }
                }
            }

            // Switch on screen size
            if (smallScreen) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Show detail if item is selected
                    selected?.also {
                        listDetailScope.detail(it)
                    } ?: run {
                        // Show list with on select logic.
                        listDetailScope.list(listDetailScope.items) { selection ->
                            navController.navigate(route = NavGraph.Route.Detail.navigateRoute(keyProvider(selection))) {
                                popUpTo(NavGraph.Route.Detail.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }

                // Only intercept back presses if an item is selected. This allows outside nav host to handle backs.
                BackHandler(selected != null) {
                    navController.popBackStack()
                }
            } else {
                // On large screens, display both list and detail
                Row(Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        listDetailScope.list(listDetailScope.items) { selection ->
                            navController.navigate(route = NavGraph.Route.Detail.navigateRoute(keyProvider(selection))) {
                                popUpTo(NavGraph.Route.Detail.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        listDetailScope.detail(selected)
                    }
                }
            }
        }
    }
}
