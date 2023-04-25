package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity.Companion.EMPTY_TOOLBAR_TITLE
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.toState
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.toState
import com.bottlerocketstudios.brarchitecture.ui.featuretoggle.featureTogglesComposable
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.toState
import com.bottlerocketstudios.brarchitecture.ui.profile.ProfileViewModel
import com.bottlerocketstudios.brarchitecture.ui.profile.toState
import com.bottlerocketstudios.brarchitecture.ui.projects.projectsComposable
import com.bottlerocketstudios.brarchitecture.ui.pullrequests.PullRequestViewModel
import com.bottlerocketstudios.brarchitecture.ui.pullrequests.toState
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.repositoryBranchesComposable
import com.bottlerocketstudios.brarchitecture.ui.repository.repositoryCommitsComposable
import com.bottlerocketstudios.brarchitecture.ui.repository.toState
import com.bottlerocketstudios.brarchitecture.ui.snippet.snippetListDetailComposable
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.navigateAsTopLevel
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.profile.ProfileScreen
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreen
import com.bottlerocketstudios.compose.repository.FileBrowserScreen
import com.bottlerocketstudios.compose.repository.RepositoryBrowserScreen
import com.bottlerocketstudios.compose.splash.SplashScreen
import com.bottlerocketstudios.launchpad.compose.util.LaunchCollection
import com.google.accompanist.web.rememberWebViewNavigator
import org.koin.androidx.compose.getViewModel

private fun NavGraphBuilder.splashComposable(navController: NavController, controls: MainWindowControls) {
    composable(Routes.Splash) {
        val vm: SplashViewModel = getViewModel()
        SplashScreen()

        // Update top level controls
        controls.reset()

        vm.authEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.Home) }
        vm.unAuthEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.AuthCode) }
    }
}

private fun NavGraphBuilder.authCodeComposable(navController: NavController, controls: MainWindowControls) {
    composable(Routes.AuthCode) {
        val webViewNavigator = rememberWebViewNavigator()

        // Update top level controls
        controls.reset()
        controls.navIntercept = {
            webViewNavigator.canGoBack.also {
                if (it) webViewNavigator.navigateBack()
            }
        }

        val vm: AuthCodeViewModel = getViewModel()
        AuthCodeScreen(
            state = vm.toState { showToolbar: Boolean ->
                controls.title = if (showToolbar) EMPTY_TOOLBAR_TITLE else ""
                controls.navIntercept = {
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

        vm.devOptionsEvent.LaunchCollection { navController.navigate(Routes.DevOptions) }
        vm.homeEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.Home) }
    }
}

private fun NavGraphBuilder.devOptionsComposable(navController: NavController, controls: MainWindowControls) {
    composable(Routes.DevOptions) {
        val viewModel: DevOptionsViewModel = getViewModel()
        val activityViewModel: ComposeActivityViewModel = getViewModel()

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.dev_options_title)

        DevOptionsScreen(state = viewModel.toState())

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (!activityViewModel.devOptionsEnabled) {
            navController.navigateUp()
        }

        viewModel.featureToggleClicked.LaunchCollection {
            navController.navigate(Routes.FeatureToggles)
        }
    }
}

private fun NavGraphBuilder.homeComposable(navController: NavController, controls: MainWindowControls) {
    composable(Routes.Home) {
        val viewModel: HomeViewModel = getViewModel()
        val activityViewModel: ComposeActivityViewModel = getViewModel()

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.home_title)
        controls.topLevel = true

        HomeScreen(state = viewModel.toState())

        viewModel.itemSelected.LaunchCollection {
            activityViewModel.selectedRepo.value = it.repo
            navController.navigate(
                Routes.RepositoryBrowser(
                    RepositoryBrowserData(repoName = it.repo.name ?: "")
                )
            )
        }
    }
}

private fun NavGraphBuilder.repositoryFileComposable(controls: MainWindowControls) {
    composable(
        route = Routes.RepositoryFile(),
        arguments = listOf(
            navArgument("hash") {},
            navArgument("path") {},
            navArgument("mimeType") { nullable = true },
        )
    ) { backStackEntry ->
        val data = RepositoryFileData(
            hash = backStackEntry.arguments?.getString("hash") ?: "",
            path = backStackEntry.arguments?.getString("path") ?: "",
            mimeType = backStackEntry.arguments?.getString("mimeType") ?: "",
        )

        val viewModel: RepositoryFileViewModel = getViewModel()
        val activityViewModel: ComposeActivityViewModel = getViewModel()

        // Update top level controls
        controls.reset()
        controls.title = data.path

        FileBrowserScreen(state = viewModel.toState())
        activityViewModel.selectedRepo.LaunchCollection { repo ->
            viewModel.loadFile(
                repo.workspace?.slug ?: "",
                repo.name ?: "",
                data.mimeType,
                data.hash,
                data.path
            )
        }

    }
}

private fun NavGraphBuilder.repositoryBrowserComposable(navController: NavController, controls: MainWindowControls) {
    composable(
        route = Routes.RepositoryBrowser(),
        arguments = listOf(
            navArgument("repoName") { defaultValue = "" },
            navArgument("folderHash") { nullable = true },
            navArgument("folderPath") { nullable = true },
        )
    ) { backStackEntry ->
        val data = RepositoryBrowserData(
            repoName = backStackEntry.arguments?.getString("repoName") ?: "",
            folderHash = backStackEntry.arguments?.getString("folderHash"),
            folderPath = backStackEntry.arguments?.getString("folderPath")
        )

        val viewModel: RepositoryBrowserViewModel = getViewModel()
        RepositoryBrowserScreen(state = viewModel.toState())
        viewModel.getFiles(data)

        // Update top level controls
        controls.reset()
        controls.title = data.folderPath ?: data.repoName
        controls.topLevel = true

        viewModel.directoryClickedEvent.LaunchCollection {
            navController.navigate(Routes.RepositoryBrowser(it))
        }

        viewModel.fileClickedEvent.LaunchCollection {
            navController.navigate(Routes.RepositoryFile(it))
        }
    }
}

private fun NavGraphBuilder.profileComposable( navController: NavController, controls: MainWindowControls) {
    composable(Routes.Profile) {
        val vm: ProfileViewModel = getViewModel()
        ProfileScreen(state = vm.toState())

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.profile_title)
        controls.topLevel = true

        vm.onLogout.LaunchCollection {
            navController.navigateAsTopLevel(Routes.AuthCode)
        }
    }
}

private fun NavGraphBuilder.pullRequestsComposable(controls: MainWindowControls) {
    composable(Routes.PullRequests) {
        val vm: PullRequestViewModel = getViewModel()
        PullRequestScreen(state = vm.toState())

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.pull_requests)
        controls.topLevel = true
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavController, mainWindowControls: MainWindowControls, widthSize: WindowWidthSizeClass) {
    navigation(startDestination = Routes.Splash, route = Routes.Main) {
        splashComposable(navController, mainWindowControls)
        authCodeComposable(navController, mainWindowControls)
        devOptionsComposable(navController, mainWindowControls)
        featureTogglesComposable(navController, mainWindowControls)
        homeComposable(navController, mainWindowControls)
        repositoryBrowserComposable(navController, mainWindowControls)
        repositoryCommitsComposable(mainWindowControls)
        repositoryBranchesComposable(mainWindowControls)
        repositoryFileComposable(mainWindowControls)
        profileComposable(navController, mainWindowControls)
        snippetListDetailComposable(mainWindowControls, widthSize)
        pullRequestsComposable(mainWindowControls)
        projectsComposable(mainWindowControls)
    }
}
