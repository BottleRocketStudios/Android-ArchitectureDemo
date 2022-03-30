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
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashFragmentViewModel
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.splash.SplashScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavController, activity: ComposeActivity) {
    with (activity) {
        navigation(startDestination = Routes.Splash, route = Routes.Main) {
            composable(Routes.Splash) {
                val fragmentViewModel: SplashFragmentViewModel = getViewModel()
                fragmentViewModel.HandleRouting(navController) {
                    SplashScreen()
                }
            }

            composable(Routes.AuthCode) {
                val viewModel: AuthCodeViewModel = getViewModel()
                viewModel.HandleRouting(navController = navController) {
                    AuthCodeScreen(state = viewModel.toState())
                }
            }

            composable(Routes.DevOptions) {
                val viewModel: DevOptionsViewModel = getViewModel()
                viewModel.HandleRouting(navController = navController) {
                    DevOptionsScreen(state = viewModel.toState())
                }
            }

            composable(Routes.Home) {
                val viewModel: HomeViewModel = getViewModel()
                viewModel.HandleRouting(navController = navController) {
                    HomeScreen(state = viewModel.toState(), selectItem = {
                        // FIXME - Connect selectItem code
                    })
                }
            }

        }
    }
}
