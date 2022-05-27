package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bottlerocketstudios.brarchitecture.R
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
import com.google.accompanist.web.WebViewNavigator
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun NavOptionsBuilder.popToMainInclusive() {
    popUpTo(Routes.Main) {
        inclusive = true
    }
}

private fun ComposeActivity.splashComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.Splash) {
        val vm: SplashViewModel = getViewModel()
        vm.ConnectBaseViewModel { SplashScreen() }

        vm.authEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.Home) }
        vm.unAuthEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.AuthCode) }
    }
}

private fun ComposeActivity.authCodeComposable(navGraphBuilder: NavGraphBuilder, navController: NavController, webViewNavigator: WebViewNavigator) {
    navGraphBuilder.composable(Routes.AuthCode) {
        val vm: AuthCodeViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            AuthCodeScreen(
                state = it.toState { showToolbar: Boolean ->
                    controls.title = if (showToolbar) ComposeActivity.EMPTY_TOOLBAR_TITLE else ""
                    navIntercept = {
                        if (vm.requestUrl.value.isNotEmpty()) {
                            vm.requestUrl.value = ""
                            true
                        } else {
                            false
                        }
                    }
                },
                navigator = webViewNavigator
            )
        }

        vm.devOptionsEvent.LaunchCollection { navController.navigate(Routes.DevOptions) }
        vm.homeEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.Home) }
    }
}

private fun ComposeActivity.devOptionsComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.DevOptions) {
        val viewModel: DevOptionsViewModel = getViewModel()
        viewModel.ConnectBaseViewModel {
            DevOptionsScreen(state = it.toState())
        }

        // TODO - integrate this check somewhere.  Probably Viewmodel with event.
        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        // if (buildConfigProvider.isProductionReleaseBuild) {
        // NOTE: Special case usage of findNavController
        // findNavController().popBackStack()
        // return
        // }

        controls.title = stringResource(id = R.string.dev_options_title)
    }
}

private fun ComposeActivity.homeComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.Home) {
        val viewModel: HomeViewModel = getViewModel()

        viewModel.ConnectBaseViewModel {
            HomeScreen(state = it.toState())
        }

        controls.title = stringResource(id = R.string.home_title)
        controls.topLevel = true

        viewModel.itemSelected.LaunchCollection{
            activityViewModel.selectedRepo.value = it.repo
        //     TODO - Navigate to browse fragment
            //     val action = HomeFragmentDirections.actionHomeToRepositoryBrowserFragment(
            //         RepositoryBrowserData(
            //             repoName = userRepositoryUiModel.repo.name ?: ""
            //         )
            //     )

        }
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavController, webViewNavigator: WebViewNavigator, activity: ComposeActivity) {
    with(activity) {
        navigation(startDestination = Routes.Splash, route = Routes.Main) {
            splashComposable(this, navController)
            authCodeComposable(this, navController, webViewNavigator)
            devOptionsComposable(this, navController)
            homeComposable(this, navController)
        }
    }
}
