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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class ComposeActivity : ComponentActivity() {
    private val activityViewModel: MainActivityViewModel by viewModel()

    var title: String
        get() = activityViewModel.title.value
        set(value) { activityViewModel.title.value = value }

    @Composable
    fun <T : BaseViewModel> T.ConnectBaseViewModel(block: @Composable (T) -> Unit) {
        // Perform resets of MainActivityVIewModel fields here.
        title = ""

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
