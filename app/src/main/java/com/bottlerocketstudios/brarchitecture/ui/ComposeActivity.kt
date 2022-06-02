package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.utils.MutableStateFlowDelegate
import com.bottlerocketstudios.compose.navdrawer.NavDrawer
import com.bottlerocketstudios.compose.navdrawer.NavItemState
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.widgets.AppBar
import com.google.accompanist.web.rememberWebViewNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeActivity : ComponentActivity() {
    val activityViewModel: MainActivityViewModel by viewModel()

    /**
     *   EMPTY_TOOLBAR_TITLE is used to show toolbar without a title.
     */
    companion object {
        const val EMPTY_TOOLBAR_TITLE = " "
    }

    // Lazy initialized public interface that provides access to view model
    val controls by lazy { Controls(activityViewModel) }
    class Controls(val viewModel: MainActivityViewModel) {
        var title by MutableStateFlowDelegate(viewModel.title)
        var topLevel by MutableStateFlowDelegate((viewModel.topLevel))
    }

    var navIntercept: (() -> Boolean)? = null

    @Composable
    fun <T : BaseViewModel> T.ConnectBaseViewModel(block: @Composable (T) -> Unit) {
        // Reset Controls
        controls.title = ""
        controls.topLevel = false
        navIntercept = null

        // Connect external routing to activity
        launchIO {
            externalNavigationEvent.asFlow().collect { runOnMain { it.navigate(this@ComposeActivity) } }
        }

        block.invoke(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val webViewNavigator = rememberWebViewNavigator()
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry.value?.destination?.route
            // TODO - try this without derived state of to see if it updates,
            val navItems = remember(currentRoute) {
                derivedStateOf {
                    generateNavDrawerItems(navController = navController, currentRoute = currentRoute ?: "")
                }
            }

            // TODO - Move appbar definition to compose module
            ArchitectureDemoTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AnimatedVisibility(
                            visible = activityViewModel.showToolbar.collectAsState(false).value,
                            enter = slideInVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessHigh)
                            ),
                            exit = slideOutVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessHigh)
                            )
                        ) {
                            val topLevel = activityViewModel.topLevel.collectAsState()

                            AppBar(
                                title = activityViewModel.title.collectAsState().value,
                                navIcon = if (topLevel.value) Icons.Default.Menu else Icons.Default.ArrowBack,
                                onNavClicked = {
                                    // If user is at top of navigation, show menu
                                    if (topLevel.value) {
                                        scope.launch {
                                            scaffoldState.drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    } else {
                                        // TODO - Observe behavior with multiple web view screens to see if WebViewNavigator works correctly
                                        //    Other possible issues: leaving WebView before navigating to top, etc.
                                        // Otherwise navigate upwards.
                                        // FIXME - Not working after login,  prolly due to webViewNav....
                                        if (webViewNavigator.canGoBack) {
                                            webViewNavigator.navigateBack()
                                        } else {
                                            if (navIntercept?.invoke() != true) {
                                                navController.popBackStack()
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    },
                    drawerContent = {
                        NavDrawer(activityViewModel.toNavDrawerState(navItems))
                    },
                ) {
                    NavHost(navController = navController, startDestination = Routes.Main) {
                        mainNavGraph(navController = navController, webViewNavigator = webViewNavigator, activity = this@ComposeActivity)
                    }
                }
            }
        }
    }

    // TODO - look into making objects under NavItem with predefined values.
    //  TODO - can we pass in NavController and route to simplify definitions??
    private fun generateNavDrawerItems(navController: NavController, currentRoute: String) =
        listOf(
            NavItemState(
                icon = R.drawable.ic_home,
                itemText = R.string.home_title,
                selected = currentRoute == Routes.Home
            ) {
                navController.navigate(Routes.Home)
            },
            NavItemState(
                icon = R.drawable.ic_snippet,
                itemText = R.string.snippets_title,
                selected = currentRoute == Routes.Snippets
            ) {
                navController.navigate(Routes.Snippets)
            },
            NavItemState(
                icon = R.drawable.ic_nav_profile,
                itemText = R.string.profile_title,
                selected = currentRoute == Routes.Profile
            ) {
                navController.navigate(Routes.Profile)
            }
        )
}
