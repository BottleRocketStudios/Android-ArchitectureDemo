package com.bottlerocketstudios.brarchitecture.ui.util.nav

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import androidx.savedstate.write
import com.bottlerocketstudios.brarchitecture.ui.NavKey
import timber.log.Timber

/**
 * https://github.com/android/nav3-recipes project by a Google Dev came in handy. Methods for this
 * class match old NavController methods, so we can migrate easily.
 */
@SuppressLint("RestrictedApi")
class Navigator(
        private var startRoute: NavKey,
        private var shouldPrintDebugInfo: Boolean = false,
        private var canTopLevelRoutesExistTogether: Boolean = false,
) {
    val backStack = mutableStateListOf(startRoute)
    var topLevelRoute by mutableStateOf(startRoute)
        private set

    // Maintain a stack for each top level route
    private val topLevelStacks = mutableMapOf(startRoute to mutableListOf(startRoute))

    // Maintain a map of shared routes to their parent stacks
    private var sharedRoutes: MutableMap<NavKey, NavKey> = mutableMapOf()

    private fun updateBackStack() {
        backStack.apply {
            clear()
            val entries = topLevelStacks.flatMap { it.value }
            addAll(entries)
        }
        printBackStack()
    }

    private fun printBackStack() {
        if (!shouldPrintDebugInfo) return
        navlog("Backstack update: ${backStack.map { it::class.simpleName }}")
    }

    private fun navlog(message: String) {
        Timber.tag("NAV3").d(message)
    }

    private fun addTopLevel(route: NavKey) {
        if (canTopLevelRoutesExistTogether) {
            // Get the existing stack or create a new one.
            val topLevelStack = topLevelStacks.remove(route) ?: mutableListOf(route)
            topLevelStacks[route] = topLevelStack
            navlog("Added top level route $route in existing stacks")
        } else {
            // Get the existing stack or create a new one.
            val topLevelStack = topLevelStacks.remove(route) ?: mutableListOf(route)
            clearAllExceptStartStack()

            topLevelStacks[route] = topLevelStack
            navlog("Added top level route $route")
        }
        topLevelRoute = route
    }

    /**
     * TODO We generally use Splash as starting route, so we should remove Splash from back stack
     * and make Home as a top level route.
     */
    private fun clearAllExceptStartStack() {
        // Remove all other top level stacks, except the start stack
        val startStack = topLevelStacks[startRoute] ?: mutableListOf(startRoute)
        topLevelStacks.clear()
        topLevelStacks[startRoute] = startStack
    }

    private fun add(route: NavKey) {
        navlog("Attempting to add $route")
        if (route is NavKey.TopLevel) {
            navlog("$route is a top level route")
            addTopLevel(route)
        } else {
            if (route is NavKey.Shared) {
                navlog("$route is a shared route")
                // If the key is already in a stack, remove it
                val oldParent = sharedRoutes[route]
                if (oldParent != null) {
                    topLevelStacks[oldParent]?.remove(route)
                }
                sharedRoutes[route] = topLevelRoute
            } else {
                navlog("$route is a normal route")
            }
            val hasBeenAdded = topLevelStacks[topLevelRoute]?.add(route) ?: false
            navlog("Added $route to $topLevelRoute stack: $hasBeenAdded")
        }
    }

    /** Navigate to the given route. */
    fun navigate(route: NavKey) {
        add(route)
        updateBackStack()
    }

    /** This combines [popBackStackTo] and [navigate]. */
    fun navigate(popBackTo: NavKey, destinationRoute: NavKey) {
        popBackStackTo(popBackTo)
        navigate(destinationRoute)
    }

    /**
     * Pop back stack one level and navigate to the new [route]. Use this for navigating from
     * dialogs.
     */
    fun navigateAndPopBackStack(route: NavKey) {
        // remove the dialog fro back stack
        popBackStack()

        // add the new route
        add(route)
        updateBackStack()
    }

    /** Go back to the previous route. */
    fun popBackStack() {
        if (backStack.size <= 1) {
            return
        }
        val removedKey = topLevelStacks[topLevelRoute]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelRoute = topLevelStacks.keys.last()
        updateBackStack()
    }

    /**
     * Go back up to the given route. This allows us to skip some routes when popping back stack.
     */
    fun popBackStackTo(route: NavKey) {
        if (backStack.size <= 1) {
            return
        }
        do {
            val removedKey = topLevelStacks[topLevelRoute]?.removeLastOrNull()
            // If the removed key was a top level key, remove the associated top level stack
            topLevelStacks.remove(removedKey)
        } while (topLevelStacks[topLevelRoute]?.isNotEmpty() == true &&
                topLevelStacks[topLevelRoute]?.lastOrNull() != route)
        topLevelRoute = topLevelStacks.keys.last()
        updateBackStack()
    }

    /** Clear the backstack and replace it with the given route. */
    fun clearAndNavigate(route: NavKey) {
        topLevelStacks.clear()
        topLevelStacks[route] = mutableListOf(route)
        topLevelRoute = route
        updateBackStack()
    }

    companion object {
        private const val KEY_START_ROUTE = "start_route"
        private const val KEY_CAN_TOP_LEVEL_ROUTES_EXIST_TOGETHER =
                "can_top_level_routes_exist_together"
        private const val KEY_SHOULD_PRINT_DEBUG_INFO = "should_print_debug_info"
        private const val KEY_TOP_LEVEL_ROUTE = "top_level_route"
        private const val KEY_TOP_LEVEL_STACK_IDS = "top_level_stack_ids"
        private const val KEY_TOP_LEVEL_STACK_KEY_PREFIX = "top_level_stack_key_"
        private const val KEY_TOP_LEVEL_STACK_VALUES_PREFIX = "top_level_stack_values_"
        private const val KEY_SHARED_ROUTES_KEYS = "shared_routes_keys"
        private const val KEY_SHARED_ROUTES_VALUES = "shared_routes_values"

        val Saver =
                Saver<Navigator, SavedState>(
                        save = { navigator ->
                            val savedState = SavedState()
                            savedState.write {
                                putSavedState(
                                        KEY_START_ROUTE,
                                        encodeToSavedState(navigator.startRoute)
                                )
                                putBoolean(
                                        KEY_SHOULD_PRINT_DEBUG_INFO,
                                        navigator.shouldPrintDebugInfo
                                )
                                putBoolean(
                                        KEY_CAN_TOP_LEVEL_ROUTES_EXIST_TOGETHER,
                                        navigator.canTopLevelRoutesExistTogether
                                )
                                putSavedState(
                                        KEY_TOP_LEVEL_ROUTE,
                                        encodeToSavedState(navigator.topLevelRoute)
                                )

                                var id = 0
                                val ids = mutableListOf<Int>()

                                for ((key, stackValues) in navigator.topLevelStacks) {
                                    putSavedState(
                                            "$KEY_TOP_LEVEL_STACK_KEY_PREFIX$id",
                                            encodeToSavedState(key)
                                    )
                                    putSavedStateList(
                                            "$KEY_TOP_LEVEL_STACK_VALUES_PREFIX$id",
                                            stackValues.map { encodeToSavedState(it) }
                                    )
                                    ids.add(id)
                                    id++
                                }

                                putIntList(KEY_TOP_LEVEL_STACK_IDS, ids)

                                val sharedRouteKeys = navigator.sharedRoutes.keys.toList()
                                val sharedRouteValues = navigator.sharedRoutes.values.toList()
                                putSavedStateList(
                                        KEY_SHARED_ROUTES_KEYS,
                                        sharedRouteKeys.map { encodeToSavedState(it) }
                                )
                                putSavedStateList(
                                        KEY_SHARED_ROUTES_VALUES,
                                        sharedRouteValues.map { encodeToSavedState(it) }
                                )
                            }
                            savedState
                        },
                        restore = { savedState ->
                            savedState.read {
                                val restoredStartRoute =
                                        decodeFromSavedState<NavKey>(getSavedState(KEY_START_ROUTE))
                                val restoredCanTopLevelRoutesExistTogether =
                                        getBoolean(KEY_CAN_TOP_LEVEL_ROUTES_EXIST_TOGETHER)
                                val restoredShouldPrintDebugInfo =
                                        getBoolean(KEY_SHOULD_PRINT_DEBUG_INFO)

                                val navigator =
                                        Navigator(
                                                startRoute = restoredStartRoute,
                                                shouldPrintDebugInfo = restoredShouldPrintDebugInfo,
                                                canTopLevelRoutesExistTogether =
                                                        restoredCanTopLevelRoutesExistTogether
                                        )

                                navigator.topLevelRoute =
                                        decodeFromSavedState(getSavedState(KEY_TOP_LEVEL_ROUTE))

                                val ids = getIntList(KEY_TOP_LEVEL_STACK_IDS)
                                for (id in ids) {
                                    // get the key and the value list
                                    val key: NavKey =
                                            decodeFromSavedState(
                                                    getSavedState(
                                                            "$KEY_TOP_LEVEL_STACK_KEY_PREFIX$id"
                                                    )
                                            )
                                    val stackValues =
                                            getSavedStateList(
                                                            "$KEY_TOP_LEVEL_STACK_VALUES_PREFIX$id"
                                                    )
                                                    .map { decodeFromSavedState<NavKey>(it) }
                                    navigator.topLevelStacks[key] = stackValues.toMutableList()
                                }

                                val encodedSharedRouteKeys =
                                        getSavedStateListOrNull(KEY_SHARED_ROUTES_KEYS)
                                val encodedSharedRouteValues =
                                        getSavedStateListOrNull(KEY_SHARED_ROUTES_VALUES)

                                if (encodedSharedRouteKeys != null &&
                                                encodedSharedRouteValues != null &&
                                                encodedSharedRouteKeys.size ==
                                                        encodedSharedRouteValues.size
                                ) {
                                    val restoredKeys =
                                            encodedSharedRouteKeys.map {
                                                decodeFromSavedState<NavKey>(it)
                                            }
                                    val restoredValues =
                                            encodedSharedRouteValues.map {
                                                decodeFromSavedState<NavKey>(it)
                                            }
                                    navigator.sharedRoutes.clear()
                                    for (i in restoredKeys.indices) {
                                        navigator.sharedRoutes[restoredKeys[i]] = restoredValues[i]
                                    }
                                }
                                navigator.updateBackStack()
                                navigator
                            }
                        }
                )
    }
}

@Composable
fun rememberNavigator(
        startRoute: NavKey,
        shouldPrintDebugInfo: Boolean = false,
        canTopLevelRoutesExistTogether: Boolean = false,
) =
        rememberSaveable(saver = Navigator.Saver) {
            Navigator(
                    startRoute = startRoute,
                    shouldPrintDebugInfo = shouldPrintDebugInfo,
                    canTopLevelRoutesExistTogether = canTopLevelRoutesExistTogether,
            )
        }
