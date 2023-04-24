package com.bottlerocketstudios.brarchitecture.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.compose.appbar.ArchAppBar
import com.bottlerocketstudios.compose.navdrawer.NavDrawer
import com.bottlerocketstudios.compose.navdrawer.NavItemState
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeActivity : ComponentActivity() {
    private val activityViewModel: ComposeActivityViewModel by viewModel()
    private val featureToggleRepository: FeatureToggleRepository by inject()

    /**
     *  Values for showing nav drawer items based on feature toggles.
     */
    private val booleanFeatureFlags = featureToggleRepository.featureToggles.value.filterIsInstance<FeatureToggle.ToggleValueBoolean>()
    private val showSnippets = booleanFeatureFlags.find { it.name == "SHOW_SNIPPETS" }?.value ?: false
    private val showPullRequests = booleanFeatureFlags.find { it.name == "SHOW_PULL_REQUESTS" }?.value ?: false
    //private val webviewConfig = featureToggleRepository.featureToggles.value.filterIsInstance<FeatureToggle.ToggleValueEnum>().find { it.name == "WEBVIEW_CONFIGURATION }?.value

    /**
     * Same as above set, but using remote config to fetch values.
     */
    /*private val booleanFeatureFlagsRemote = featureToggleRepository.featureToggles.value.filterIsInstance<FeatureToggle.ToggleValueBoolean>()
    private val showSnippetsRemote = booleanFeatureFlagsRemote.find { it.name == "SHOW_SNIPPETS" }?.value ?: false
    private val showPullsRemote = booleanFeatureFlagsRemote.find { it.name == "SHOW_PULL_REQUESTS" }?.value ?: false
    private val webviewConfigRemote = featureToggleRepository.featureTogglesByRemoteConfig.value.filterIsInstance<FeatureToggle.ToggleValueEnum>().find { it.name == "WEBVIEW_CONFIGURATION" }?.value*/

    /**
     *   EMPTY_TOOLBAR_TITLE is used to show toolbar without a title.
     */
    companion object {
        const val EMPTY_TOOLBAR_TITLE = " "
    }

    // Lazy initialized public interface that provides access to view model
    private val controls by lazy { MainWindowControlsImplementation(activityViewModel, navIntercept) }

    // Real time intercept of nav events.  Return true if processed and should skip default behavior.
    private val navIntercept: MutableState<(() -> Boolean)?> = mutableStateOf(null)

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            App(widthSizeClass)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun App(widthSize: WindowWidthSizeClass) {
        val coroutineScope = rememberCoroutineScope()
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        val navItems = remember(currentRoute) {
            derivedStateOf {
                generateNavDrawerItems(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    currentRoute = currentRoute.orEmpty(),
                    showHomeSubList = !activityViewModel.selectedRepo.value.name.isNullOrBlank()
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
                        navController = navController,
                        navIntercept = navIntercept.value
                    )
                },
                drawerContent = {
                    NavDrawer(
                        activityViewModel.toNavDrawerState(navItems) {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            navController.navigate(Routes.DevOptions)
                        }
                    )
                },
            ) {
                NavHost(navController = navController, startDestination = Routes.Main) {
                    mainNavGraph(
                        navController = navController,
                        mainWindowControls = controls,
                        widthSize = widthSize)
                }
            }
        }
    }

    // TODO - look into making objects under NavItem with predefined values.
    //  TODO - can we pass in NavController and route to simplify definitions??
    @Suppress("LongMethod")
    private fun generateNavDrawerItems(navController: NavController, scaffoldState: ScaffoldState, currentRoute: String, showHomeSubList: Boolean) =
        listOfNotNull(
            NavItemState(
                icon = R.drawable.ic_home,
                itemText = R.string.home_title,
                selected = getTopRoute(currentRoute) == Routes.Home,
                nestedMenuItems = if (showHomeSubList && getTopRoute(currentRoute) == Routes.Home) listOf(
                    NavItemState(
                        icon = R.drawable.ic_source,
                        itemText = R.string.home_nav_source,
                        selected = currentRoute == Routes.RepositoryBrowser(),
                    ) {
                        scaffoldState.drawerState.close()
                        navController.navigate(Routes.RepositoryBrowser(RepositoryBrowserData(repoName = activityViewModel.selectedRepo.value.name ?: "")))
                    },
                    NavItemState(
                        icon = R.drawable.ic_commit,
                        itemText = R.string.home_nav_commits,
                        selected = currentRoute == Routes.Commits,
                    ) {
                        scaffoldState.drawerState.close()
                        navController.navigate(Routes.Commits)
                    },
                    NavItemState(
                        icon = R.drawable.ic_branch,
                        itemText = R.string.home_nav_branches,
                        selected = currentRoute == Routes.Branches,
                    ) {
                        scaffoldState.drawerState.close()
                        navController.navigate(Routes.Branches)
                    }
                ) else emptyList()
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.Home)
            },
            NavItemState(
                icon = R.drawable.ic_projects,
                itemText = R.string.projects,
                selected = currentRoute == Routes.Projects
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.Projects)
            },
            NavItemState(
                icon = R.drawable.ic_snippet,
                itemText = R.string.snippets_title,
                selected = currentRoute == Routes.Snippets
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.Snippets)
            }.takeIf { showSnippets },
            NavItemState(
                icon = R.drawable.ic_nav_profile,
                itemText = R.string.profile_title,
                selected = currentRoute == Routes.Profile
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.Profile)
            },
            NavItemState(
                icon = R.drawable.ic_pull_request,
                itemText = R.string.pull_requests,
                selected = currentRoute == Routes.PullRequests
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.PullRequests)
            }.takeIf { showPullRequests },
        )

    private fun getTopRoute(route: String) =
        when (route) {
            Routes.Home,
            Routes.Commits,
            Routes.Branches,
            Routes.RepositoryBrowser()
            -> Routes.Home
            else -> Routes.Main
        }
}
