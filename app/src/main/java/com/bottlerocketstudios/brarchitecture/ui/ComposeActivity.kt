package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("FunctionName")
object Routes {
    const val Main = "main"
    const val Home = "home"
    const val Splash = "splash"
    const val AuthCode = "authcode"
    const val DevOptions = "devoptions"

    // Example path with arguments; will remove after first real path with arguments is in place.
    fun UserProfile(id: String) = "profile/{${id}}"
}


class ComposeActivity : ComponentActivity() {
    private val activityViewModel: MainActivityViewModel by viewModel()

    var title: String
        get() = activityViewModel.title.value
        set(value) { activityViewModel.title.value = value }

    @Composable
    fun <T : BaseViewModel> T.ConnectBaseViewModel(navController: NavController, block: @Composable (T) -> Unit) {
        // Perform resets of MainActivityVIewModel fields here.
        // title = ""

        // Connect external routing to activiety
        launchIO {
            externalNavigationEvent.asFlow().collect { runOnMain { it.navigate(this@ComposeActivity) } }
        }

        block.invoke(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ArchitectureDemoTheme {
                Scaffold(
                    topBar = {
                        if (activityViewModel.showToolbar.collectAsState().value) {
                            TopAppBar {
                                Text(activityViewModel.displayName.collectAsState().value)
                            }
                        }
                    }
                ) {
                    NavHost(navController = navController, startDestination = Routes.Main) {
                        mainNavGraph(navController = navController, activity = this@ComposeActivity)
                    }
                }
            }
        }
    }
}
