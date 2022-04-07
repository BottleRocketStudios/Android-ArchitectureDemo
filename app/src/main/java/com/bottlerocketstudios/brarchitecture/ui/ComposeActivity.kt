package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.asFlow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.widgets.AppBar
import com.bottlerocketstudios.compose.widgets.OutlinedSurfaceButton
import com.google.accompanist.web.rememberWebViewNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KProperty

class MutableStateFlowDelegate<T>(val flow: MutableStateFlow<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = flow.value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        flow.value = value
    }
}


class ComposeActivity : ComponentActivity() {
    private val activityViewModel: MainActivityViewModel by viewModel()

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
                                        // Otherwise navigate upwards.
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
                        OutlinedSurfaceButton(
                            buttonText = stringResource(id = R.string.dev_options_button),
                            forceCaps = true,
                            onClick = { navController.navigate(Routes.DevOptions) },
                            modifier = Modifier.padding(top = Dimens.grid_4)
                        )
                    },

                    ) {
                    NavHost(navController = navController, startDestination = Routes.Main) {
                        mainNavGraph(navController = navController, webViewNavigator = webViewNavigator, activity = this@ComposeActivity)
                    }
                }
            }
        }
    }
}
