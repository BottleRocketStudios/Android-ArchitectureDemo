package com.bottlerocketstudios.brarchitecture.ui

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.bottlerocketstudios.brarchitecture.ui.util.nav.Navigator
import com.bottlerocketstudios.compose.appbar.ArchAppBar
import com.bottlerocketstudios.compose.navdrawer.NavDrawer
import com.bottlerocketstudios.compose.navdrawer.NavItemState
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.widgets.FullScreenLoadingIndicator
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ArchApp(
        widthSize: WindowWidthSizeClass,
        activityViewModel: ComposeActivityViewModel,
        navigator: Navigator,
        controls: MainWindowControls,
        navIntercept: MutableState<(() -> Boolean)?>,
        bottomBar: @Composable () -> Unit
) {
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

        val currentRoute = navigator.backStack.lastOrNull() ?: Routes.Splash
        val navItems =
                remember(currentRoute) {
                        derivedStateOf {
                                generateNavDrawerItems(
                                        navigator = navigator,
                                        scaffoldState = scaffoldState,
                                        coroutineScope = coroutineScope,
                                        currentRoute = currentRoute,
                                        showHomeSubList =
                                                !activityViewModel.selectedRepo.value.name
                                                        .isNullOrBlank(),
                                        activityViewModel = activityViewModel
                                )
                        }
                }

        ArchitectureDemoTheme {
                Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                                ArchAppBar(
                                        state = activityViewModel.toArchAppBarState(),
                                        scaffoldState = scaffoldState,
                                        onBackPress = { navigator.popBackStack() },
                                        navIntercept = navIntercept.value
                                )
                        },
                        drawerContent = {
                                NavDrawer(
                                        activityViewModel.toNavDrawerState(navItems) {
                                                coroutineScope.launch {
                                                        scaffoldState.drawerState.close()
                                                }
                                                navigator.navigate(Routes.DevOptions)
                                        }
                                )
                        },
                        bottomBar = bottomBar
                ) {
                        NavDisplay(
                                backStack = navigator.backStack,
                                onBack = { navigator.popBackStack() },
                                entryDecorators =
                                        listOf(
                                                rememberSaveableStateHolderNavEntryDecorator(),
                                                rememberViewModelStoreNavEntryDecorator()
                                        ),
                                entryProvider =
                                        mainNavEntryProvider(
                                                navigator = navigator,
                                                mainWindowControls = controls,
                                                widthSize = widthSize
                                        )
                        )
                }

                val showLoadingIndicator by activityViewModel.showLoadingIndicator.collectAsState()
                if (showLoadingIndicator) {
                        FullScreenLoadingIndicator()
                }
        }
}

// Ported and adapted from ComposeActivity
@Suppress("LongMethod")
private fun generateNavDrawerItems(
        navigator: Navigator,
        scaffoldState: androidx.compose.material.ScaffoldState,
        coroutineScope: kotlinx.coroutines.CoroutineScope,
        currentRoute: NavKey,
        showHomeSubList: Boolean,
        activityViewModel: ComposeActivityViewModel
) =
        listOfNotNull(
                NavItemState(
                        icon = com.bottlerocketstudios.brarchitecture.R.drawable.ic_home,
                        itemText = com.bottlerocketstudios.brarchitecture.R.string.home_title,
                        selected = getTopRoute(currentRoute) == Routes.Home,
                        nestedMenuItems =
                                if (showHomeSubList && getTopRoute(currentRoute) == Routes.Home)
                                        listOf(
                                                NavItemState(
                                                        icon =
                                                                com.bottlerocketstudios
                                                                        .brarchitecture
                                                                        .R
                                                                        .drawable
                                                                        .ic_projects,
                                                        itemText =
                                                                com.bottlerocketstudios
                                                                        .brarchitecture
                                                                        .R
                                                                        .string
                                                                        .home_nav_source,
                                                        selected =
                                                                currentRoute is
                                                                        Routes.RepositoryBrowser,
                                                ) {
                                                        coroutineScope.launch {
                                                                scaffoldState.drawerState.close()
                                                        }
                                                        navigator.navigate(
                                                                Routes.RepositoryBrowser(
                                                                        repoName =
                                                                                activityViewModel
                                                                                        .selectedRepo
                                                                                        .value
                                                                                        .name
                                                                                        ?: ""
                                                                )
                                                        )
                                                },
                                                NavItemState(
                                                        icon =
                                                                com.bottlerocketstudios
                                                                        .brarchitecture
                                                                        .R
                                                                        .drawable
                                                                        .ic_pull_request,
                                                        itemText =
                                                                com.bottlerocketstudios
                                                                        .brarchitecture
                                                                        .R
                                                                        .string
                                                                        .home_nav_commits,
                                                        selected = currentRoute == Routes.Commits,
                                                ) {
                                                        coroutineScope.launch {
                                                                scaffoldState.drawerState.close()
                                                        }
                                                        navigator.navigate(Routes.Commits)
                                                },
                                                NavItemState(
                                                        icon =
                                                                com.bottlerocketstudios
                                                                        .brarchitecture
                                                                        .R
                                                                        .drawable
                                                                        .ic_pull_request,
                                                        itemText =
                                                                com.bottlerocketstudios
                                                                        .brarchitecture
                                                                        .R
                                                                        .string
                                                                        .home_nav_branches,
                                                        selected = currentRoute == Routes.Branches,
                                                ) {
                                                        coroutineScope.launch {
                                                                scaffoldState.drawerState.close()
                                                        }
                                                        navigator.navigate(Routes.Branches)
                                                }
                                        )
                                else emptyList()
                ) {
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        navigator.navigate(Routes.Home)
                },
                NavItemState(
                        icon = com.bottlerocketstudios.brarchitecture.R.drawable.ic_projects,
                        itemText = com.bottlerocketstudios.brarchitecture.R.string.projects,
                        selected = currentRoute == Routes.Projects
                ) {
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        navigator.navigate(Routes.Projects)
                },
                NavItemState(
                                icon = com.bottlerocketstudios.brarchitecture.R.drawable.ic_snippet,
                                itemText =
                                        com.bottlerocketstudios
                                                .brarchitecture
                                                .R
                                                .string
                                                .snippets_title,
                                selected = currentRoute == Routes.Snippets
                        ) {
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        navigator.navigate(Routes.Snippets)
                }
                        .takeIf { activityViewModel.devOptionsEnabled },
                NavItemState(
                        icon = com.bottlerocketstudios.brarchitecture.R.drawable.ic_home,
                        itemText = com.bottlerocketstudios.brarchitecture.R.string.profile_title,
                        selected = currentRoute == Routes.Profile
                ) {
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        navigator.navigate(Routes.Profile)
                },
                NavItemState(
                                icon =
                                        com.bottlerocketstudios
                                                .brarchitecture
                                                .R
                                                .drawable
                                                .ic_pull_request,
                                itemText =
                                        com.bottlerocketstudios
                                                .brarchitecture
                                                .R
                                                .string
                                                .pull_requests,
                                selected = currentRoute == Routes.PullRequests
                        ) {
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        navigator.navigate(Routes.PullRequests)
                }
                        .takeIf { activityViewModel.devOptionsEnabled },
        )

private fun getTopRoute(route: NavKey) =
        when (route) {
                Routes.Home, Routes.Commits, Routes.Branches, is Routes.RepositoryBrowser ->
                        Routes.Home
                else -> Routes.Main
        }
