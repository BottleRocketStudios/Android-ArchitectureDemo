package com.bottlerocketstudios.brarchitecture.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.toState
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.toState
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.toState
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashFragmentViewModel
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.splash.SplashScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun NavOptionsBuilder.popToMainInclusive() {
    popUpTo(Routes.Main) {
        inclusive = true
    }
}

fun NavController.navigateAsTopLevel(route: String): () -> Unit = {
    navigate(route) {
        popToMainInclusive()
    }
}

fun ComposeActivity.splashComposable(
    navGraphBuilder: NavGraphBuilder,
    navController: NavController
) {
    navGraphBuilder.composable(Routes.Splash) {
        val vm: SplashFragmentViewModel = getViewModel()
        vm.onAuthenticated = navController.navigateAsTopLevel(Routes.Home)
        vm.onUnauthenticated = navController.navigateAsTopLevel(Routes.AuthCode)

        vm.ConnectBaseViewModel(navController) {
            SplashScreen()
        }
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavController, activity: ComposeActivity) {
    with(activity) {
        navigation(startDestination = Routes.Splash, route = Routes.Main) {
            splashComposable(this, navController)

            composable(Routes.AuthCode) {
                val viewModel: AuthCodeViewModel = getViewModel()
                viewModel.ConnectBaseViewModel(navController = navController) {
                    AuthCodeScreen(state = viewModel.toState())
                }
            }

            composable(Routes.DevOptions) {
                val viewModel: DevOptionsViewModel = getViewModel()
                viewModel.ConnectBaseViewModel(navController = navController) {
                    DevOptionsScreen(state = viewModel.toState())
                }
            }

            composable(Routes.Home) {
                val viewModel: HomeViewModel = getViewModel()
                viewModel.ConnectBaseViewModel(navController = navController) {
                    HomeScreen(state = viewModel.toState(), selectItem = {
                        // FIXME - Connect selectItem code
                    })
                }
            }

        }
    }
}

