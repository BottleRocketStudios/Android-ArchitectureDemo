package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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

    // Example path with arguments; will remove after first real path with arguements is in place.
    fun UserProfile(id: String) = "profile/{${id}}"
}


class ComposeActivity : ComponentActivity() {

    @Composable
    fun <T : BaseViewModel> T.HandleRouting(navController: NavController, block: @Composable (T) -> Unit) {
        // Using LaunchIO to scope collections to the lifecycle of the viewmodel.
        // TODO - Look into possible timing issues here, where events could be processed when UI for VM is not visible.
        launchIO {
            navigationEvent.asFlow().collect { runOnMain { it.navigate(navController) } }
        }
        launchIO {
            externalNavigationEvent.asFlow().collect { runOnMain { it.navigate(this@ComposeActivity) } }
        }

        block.invoke(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val activityViewModel: MainActivityViewModel by viewModel()

            ArchitectureDemoTheme {
                // TODO scaffold here.
                NavHost(navController = navController, startDestination = Routes.Main) {
                    mainNavGraph(navController = navController, activity = this@ComposeActivity)
                }
            }
        }
    }
}
