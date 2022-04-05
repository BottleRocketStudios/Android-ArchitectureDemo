package com.bottlerocketstudios.brarchitecture.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.toState
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.toState
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.toState
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.LaunchCollection
import com.bottlerocketstudios.brarchitecture.ui.util.navigateAsTopLevel
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.splash.SplashScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun ComposeActivity.splashComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.Splash) {
        val vm: SplashViewModel = getViewModel()
        vm.authEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.Home) }
        vm.unAuthEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.AuthCode) }

        vm.ConnectBaseViewModel { SplashScreen() }
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavController, activity: ComposeActivity) {
    with(activity) {
        navigation(startDestination = Routes.Splash, route = Routes.Main) {
            splashComposable(this, navController)

            composable(Routes.AuthCode) {
                val viewModel: AuthCodeViewModel = getViewModel()
                viewModel.ConnectBaseViewModel {
                    AuthCodeScreen(state = it.toState())
                }
            }

            composable(Routes.DevOptions) {
                val viewModel: DevOptionsViewModel = getViewModel()
                viewModel.ConnectBaseViewModel {
                    DevOptionsScreen(state = it.toState())
                }
            }

            composable(Routes.Home) {
                val viewModel: HomeViewModel = getViewModel()
                viewModel.ConnectBaseViewModel {
                    HomeScreen(state = it.toState(), selectItem = {
                        // FIXME - Connect selectItem code
                    })
                }
            }

        }
    }
}

