package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.toState
import com.bottlerocketstudios.brarchitecture.ui.profile.ProfileViewModel
import com.bottlerocketstudios.brarchitecture.ui.profile.toState
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.toState
import com.bottlerocketstudios.brarchitecture.ui.snippet.CreateSnippetFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetsFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.toState
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.LaunchCollection
import com.bottlerocketstudios.brarchitecture.ui.util.navigateAsTopLevel
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.profile.ProfileScreen
import com.bottlerocketstudios.compose.repository.FileBrowserScreen
import com.bottlerocketstudios.compose.repository.RepositoryBrowserScreen
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreen
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreen
import com.bottlerocketstudios.compose.splash.SplashScreen
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
            navIntercept = {
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

        viewModel.itemSelected.LaunchCollection{
            activityViewModel.selectedRepo.value = it.repo
            navController.navigate(Routes.RepositoryBrowser(
                RepositoryBrowserData(repoName = it.repo.name ?: "")
            ))
        }
    }
}

private fun ComposeActivity.repositoryFileComposable(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(
        route = "file?hash={hash}&path={path}&mimeType={mimeType}",
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

        val viewModel: RepositoryFileFragmentViewModel = getViewModel()
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
        "repository?repoName={repoName}&folderHash={folderHash}&folderPath={folderPath}",
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

        viewModel.directoryClickedEvent.LaunchCollection {
            navController.navigate(Routes.RepositoryBrowser(it))
        }

        viewModel.fileClickedEvent.LaunchCollection {
            navController.navigate(Routes.RepositoryFile(it))
        }
    }
}

private fun ComposeActivity.snippetsComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.Snippets) {
        val vm: SnippetsFragmentViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            SnippetsBrowserScreen(it.toState())
        }

        controls.title = stringResource(id = R.string.snippets_title)
        controls.topLevel = true

        vm.createClicked.LaunchCollection {
            navController.navigate(Routes.CreateSnippet)
        }

        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    vm.refreshSnippets()
                }
            }

            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }
    }
}

private fun ComposeActivity.createSnippetComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.CreateSnippet) {
        val vm: CreateSnippetFragmentViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            CreateSnippetScreen(state = it.toState())
        }

        controls.title = EMPTY_TOOLBAR_TITLE

        vm.onSuccess.LaunchCollection {
            navController.navigateUp()
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

fun NavGraphBuilder.mainNavGraph(navController: NavController, activity: ComposeActivity) {
    with(activity) {
        navigation(startDestination = Routes.Splash, route = Routes.Main) {
            splashComposable(this, navController)
            authCodeComposable(this, navController)
            devOptionsComposable(this, navController)
            homeComposable(this, navController)
            repositoryBrowserComposable(this, navController)
            repositoryFileComposable(this)
            snippetsComposable(this, navController)
            createSnippetComposable(this, navController)
            profileComposable(this, navController)
        }
    }
}
