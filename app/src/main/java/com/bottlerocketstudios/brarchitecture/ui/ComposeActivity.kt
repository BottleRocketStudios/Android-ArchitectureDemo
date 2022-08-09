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
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.utils.MutableStateFlowDelegate
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.compose.appbar.ArchAppBar
import com.bottlerocketstudios.compose.navdrawer.NavDrawer
import com.bottlerocketstudios.compose.navdrawer.NavItemState
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeActivity : ComponentActivity() {
    val activityViewModel: ComposeActivityViewModel by viewModel()

    /**
     *   EMPTY_TOOLBAR_TITLE is used to show toolbar without a title.
     */
    companion object {
        const val EMPTY_TOOLBAR_TITLE = " "
    }

    // Lazy initialized public interface that provides access to view model
    val controls by lazy { Controls(activityViewModel) }
    class Controls(viewModel: ComposeActivityViewModel) {
        var title by MutableStateFlowDelegate(viewModel.title)
        var topLevel by MutableStateFlowDelegate((viewModel.topLevel))
    }

    // This may need to be remember or state? to trigger recomposition of App Bar....
    val navIntercept: MutableState<(() -> Boolean)?> = mutableStateOf(null)

    @Composable
    fun <T : BaseViewModel> T.ConnectBaseViewModel(block: @Composable (T) -> Unit) {
        // Reset Controls
        controls.title = ""
        controls.topLevel = false
        navIntercept.value = null

        // Connect external routing to activity
        launchIO {
            externalNavigationEvent.asFlow().collect { runOnMain { it.navigate(this@ComposeActivity) } }
        }

        block.invoke(this)
    }

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
                    mainNavGraph(navController = navController, activity = this@ComposeActivity, widthSize)
                }
            }
        }
    }

    // TODO - look into making objects under NavItem with predefined values.
    //  TODO - can we pass in NavController and route to simplify definitions??
    private fun generateNavDrawerItems(navController: NavController, scaffoldState: ScaffoldState, currentRoute: String, showHomeSubList: Boolean) =
        listOf(
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
                    }
                ) else emptyList()
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.Home)
            },
            NavItemState(
                icon = R.drawable.ic_snippet,
                itemText = R.string.snippets_title,
                selected = currentRoute == Routes.Snippets
            ) {
                scaffoldState.drawerState.close()
                navController.navigate(Routes.Snippets)
            },
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
            }
        )

    private fun getTopRoute(route: String) =
        when (route) {
            Routes.Home,
            Routes.Commits,
            Routes.RepositoryBrowser()
            -> Routes.Home
            else -> Routes.Main
        }
}
