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
import com.bottlerocketstudios.brarchitecture.ui.projects.projectsComposable
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.toState
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.toState
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.toState
import com.bottlerocketstudios.brarchitecture.ui.profile.ProfileViewModel
import com.bottlerocketstudios.brarchitecture.ui.profile.toState
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
import org.koin.androidx.viewmodel.ext.android.getViewModel

private fun ComposeActivity.splashComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.Splash) {
        val vm: SplashViewModel = getViewModel()
        vm.ConnectBaseViewModel { SplashScreen() }

        vm.authEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.Home) }
        vm.unAuthEvent.LaunchCollection { navController.navigateAsTopLevel(Routes.AuthCode) }
    }
}

private fun ComposeActivity.authCodeComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.AuthCode) {
        val webViewNavigator = rememberWebViewNavigator()

        val vm: AuthCodeViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            AuthCodeScreen(
                state = it.toState { showToolbar: Boolean ->
                    controls.title = if (showToolbar) EMPTY_TOOLBAR_TITLE else ""
                    navIntercept.value = {
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
            navIntercept.value = {
                webViewNavigator.canGoBack.also {
                    if (it) webViewNavigator.navigateBack()
                }
            }
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

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (!activityViewModel.devOptionsEnabled) {
            navController.navigateUp()
        }

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

private fun ComposeActivity.repositoryFileComposable(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(
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
        viewModel.ConnectBaseViewModel {
            FileBrowserScreen(state = it.toState())
            activityViewModel.selectedRepo.LaunchCollection { repo ->
                it.loadFile(
                    repo.workspace?.slug ?: "",
                    repo.name ?: "",
                    data.mimeType,
                    data.hash,
                    data.path
                )
            }
        }

        controls.title = data.path
    }
}

private fun ComposeActivity.repositoryBrowserComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(
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
        viewModel.ConnectBaseViewModel {
            RepositoryBrowserScreen(state = it.toState())
            it.getFiles(data)
        }

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

private fun ComposeActivity.profileComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.Profile) {
        val vm: ProfileViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            ProfileScreen(state = it.toState())
        }

        controls.title = stringResource(id = R.string.profile_title)
        controls.topLevel = true

        vm.onLogout.LaunchCollection {
            navController.navigateAsTopLevel(Routes.AuthCode)
        }
    }
}

private fun ComposeActivity.pullRequestsComposable(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(Routes.PullRequests) {
        val vm: PullRequestViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            PullRequestScreen(state = it.toState())
        }

        controls.title = stringResource(id = R.string.pull_requests)
        controls.topLevel = true
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavController, activity: ComposeActivity, widthSize: WindowWidthSizeClass) {
    with(activity) {
        navigation(startDestination = Routes.Splash, route = Routes.Main) {
            splashComposable(this, navController)
            authCodeComposable(this, navController)
            devOptionsComposable(this, navController)
            homeComposable(this, navController)
            repositoryBrowserComposable(this, navController)
            repositoryCommitsComposable(this)
            repositoryBranchesComposable(this)
            repositoryFileComposable(this)
            profileComposable(this, navController)
            snippetListDetailComposable(this, widthSize)
            pullRequestsComposable(this)
            projectsComposable(this)
        }
    }
}
