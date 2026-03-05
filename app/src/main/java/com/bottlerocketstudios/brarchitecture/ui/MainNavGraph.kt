package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.toState
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.toState
import com.bottlerocketstudios.brarchitecture.ui.featuretoggle.FeatureToggleViewModel
import com.bottlerocketstudios.brarchitecture.ui.featuretoggle.toState
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.toState
import com.bottlerocketstudios.brarchitecture.ui.profile.ProfileViewModel
import com.bottlerocketstudios.brarchitecture.ui.profile.toState
import com.bottlerocketstudios.brarchitecture.ui.projects.ProjectsViewModel
import com.bottlerocketstudios.brarchitecture.ui.projects.toState
import com.bottlerocketstudios.brarchitecture.ui.pullrequests.PullRequestViewModel
import com.bottlerocketstudios.brarchitecture.ui.pullrequests.toState
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBranchesViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryCommitViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.toState
import com.bottlerocketstudios.brarchitecture.ui.snippet.CreateSnippetViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetDetailsViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetsViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.toState
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.nav.Navigator
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.bottlerocketstudios.compose.featuretoggles.FeatureToggleScreen
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.profile.ProfileScreen
import com.bottlerocketstudios.compose.projects.ProjectsScreen
import com.bottlerocketstudios.compose.pullrequest.PullRequestScreen
import com.bottlerocketstudios.compose.repository.FileBrowserScreen
import com.bottlerocketstudios.compose.repository.RepositoryBranchesScreen
import com.bottlerocketstudios.compose.repository.RepositoryBrowserScreen
import com.bottlerocketstudios.compose.repository.RepositoryCommitScreen
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreen
import com.bottlerocketstudios.compose.snippets.SnippetDetailsScreen
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreen
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreenState
import com.bottlerocketstudios.compose.splash.SplashScreen
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.launchpad.compose.util.LaunchCollection
import com.bottlerocketstudios.launchpad.compose.widgets.listdetail.AnimatedListDetail
import org.koin.androidx.compose.koinViewModel

@Suppress("LongMethod")
fun mainNavEntryProvider(
        navigator: Navigator,
        mainWindowControls: MainWindowControls,
        widthSize: WindowWidthSizeClass
): (NavKey) -> NavEntry<NavKey> = entryProvider {
    entry<Routes.Splash> { _: Routes.Splash ->
        val vm: SplashViewModel = koinViewModel()
        SplashScreen()
        mainWindowControls.reset()
        vm.authEvent.LaunchCollection { navigator.clearAndNavigate(Routes.Home) }
        vm.unAuthEvent.LaunchCollection { navigator.clearAndNavigate(Routes.AuthCode) }
    }

    entry<Routes.AuthCode> { _: Routes.AuthCode ->
        mainWindowControls.reset()
        val vm: AuthCodeViewModel = koinViewModel()
        AuthCodeScreen(
                state =
                        vm.toState { showToolbar: Boolean ->
                            mainWindowControls.title =
                                    if (showToolbar) ComposeActivity.EMPTY_TOOLBAR_TITLE else ""
                            mainWindowControls.navIntercept = {
                                if (vm.requestUrl.value.isNotEmpty()) {
                                    vm.requestUrl.value = ""
                                    true
                                } else {
                                    false
                                }
                            }
                        },
        )
        vm.devOptionsEvent.LaunchCollection { navigator.navigate(Routes.DevOptions) }
        vm.homeEvent.LaunchCollection { navigator.clearAndNavigate(Routes.Home) }
    }

    entry<Routes.DevOptions> { _: Routes.DevOptions ->
        val viewModel: DevOptionsViewModel = koinViewModel()
        val activityViewModel: ComposeActivityViewModel = koinViewModel()
        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.dev_options_title)
        DevOptionsScreen(state = viewModel.toState())
        if (!activityViewModel.devOptionsEnabled) {
            navigator.popBackStack()
        }
        viewModel.featureToggleClicked.LaunchCollection {
            navigator.navigate(Routes.FeatureToggles)
        }
    }

    entry<Routes.Home> { _: Routes.Home ->
        val viewModel: HomeViewModel = koinViewModel()
        val activityViewModel: ComposeActivityViewModel = koinViewModel()
        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.home_title)
        mainWindowControls.topLevel = true
        HomeScreen(state = viewModel.toState())
        viewModel.repositorySelected.LaunchCollection {
            activityViewModel.selectedRepo.value = it.repo
            navigator.navigate(Routes.RepositoryBrowser(repoName = it.repo.name ?: ""))
        }
    }

    entry<Routes.RepositoryBrowser> { route: Routes.RepositoryBrowser ->
        val viewModel: RepositoryBrowserViewModel = koinViewModel()
        RepositoryBrowserScreen(state = viewModel.toState())
        viewModel.getFiles(
                RepositoryBrowserData(route.repoName, route.folderHash, route.folderPath)
        )
        mainWindowControls.reset()
        mainWindowControls.title = route.folderPath ?: route.repoName
        mainWindowControls.topLevel = true
        viewModel.directoryClickedEvent.LaunchCollection {
            navigator.navigate(Routes.RepositoryBrowser(it.repoName, it.folderHash, it.folderPath))
        }
        viewModel.fileClickedEvent.LaunchCollection {
            navigator.navigate(Routes.RepositoryFile(it.hash, it.path, it.mimeType))
        }
    }

    entry<Routes.RepositoryFile> { route: Routes.RepositoryFile ->
        val viewModel: RepositoryFileViewModel = koinViewModel()
        val activityViewModel: ComposeActivityViewModel = koinViewModel()
        mainWindowControls.reset()
        mainWindowControls.title = route.path
        FileBrowserScreen(state = viewModel.toState())
        activityViewModel.selectedRepo.LaunchCollection { repo ->
            viewModel.loadFile(
                    repo.workspace?.slug ?: "",
                    repo.name ?: "",
                    route.mimeType,
                    route.hash,
                    route.path
            )
        }
    }

    entry<Routes.Profile> { _: Routes.Profile ->
        val vm: ProfileViewModel = koinViewModel()
        ProfileScreen(state = vm.toState())
        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.profile_title)
        mainWindowControls.topLevel = true
        vm.onLogout.LaunchCollection { navigator.clearAndNavigate(Routes.AuthCode) }
    }

    entry<Routes.PullRequests> { _: Routes.PullRequests ->
        val vm: PullRequestViewModel = koinViewModel()
        PullRequestScreen(state = vm.toState())
        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.pull_requests)
        mainWindowControls.topLevel = true
    }

    entry<Routes.FeatureToggles> { _: Routes.FeatureToggles ->
        val viewModel: FeatureToggleViewModel = koinViewModel()
        val activityViewModel: ComposeActivityViewModel = koinViewModel()
        FeatureToggleScreen(state = viewModel.toState())
        if (!activityViewModel.devOptionsEnabled) {
            navigator.popBackStack()
        }
        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.feature_toggles_title)
    }

    entry<Routes.Projects> { _: Routes.Projects ->
        val vm: ProjectsViewModel = koinViewModel()
        ProjectsScreen(state = vm.toState())
        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.projects)
        mainWindowControls.topLevel = true
    }

    entry<Routes.Commits> { _: Routes.Commits ->
        val viewModel: RepositoryCommitViewModel = koinViewModel()
        val activityViewModel: ComposeActivityViewModel = koinViewModel()
        val repoName = activityViewModel.selectedRepo.value.name ?: ""
        RepositoryCommitScreen(state = viewModel.toState())
        mainWindowControls.reset()
        mainWindowControls.title = repoName
        mainWindowControls.topLevel = true
        activityViewModel.selectedRepo.LaunchCollection {
            viewModel.currentRepoName.value = it.name.orEmpty()
        }
    }

    entry<Routes.Branches> { _: Routes.Branches ->
        val viewModel: RepositoryBranchesViewModel = koinViewModel()
        val activityViewModel: ComposeActivityViewModel = koinViewModel()
        mainWindowControls.reset()
        mainWindowControls.title = activityViewModel.selectedRepo.value.name ?: ""
        mainWindowControls.topLevel = true
        RepositoryBranchesScreen(state = viewModel.toState())
        activityViewModel.selectedRepo.LaunchCollection {
            viewModel.currentRepoName.value = it.name.orEmpty()
        }
    }

    entry<Routes.Snippets> { _: Routes.Snippets ->
        val snippetsViewModel: SnippetsViewModel = koinViewModel()
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        mainWindowControls.reset()
        mainWindowControls.title = stringResource(id = R.string.snippets_title)
        mainWindowControls.topLevel = true

        val list = snippetsViewModel.snippets.collectAsState(initial = emptyList())
        AnimatedListDetail(
                list = list.value + SnippetsViewModel.CreateSnippetItem,
                keyProvider = { it.id },
                compactWidth = widthSize == WindowWidthSizeClass.Compact
        ) {
            list { list ->
                SnippetsBrowserScreen(
                        state =
                                SnippetsBrowserScreenState(
                                        snippets =
                                                (list - SnippetsViewModel.CreateSnippetItem)
                                                        .asMutableState(),
                                        createVisible =
                                                snippetsViewModel.showCreateCta.collectAsState(),
                                        onCreateSnippetClicked = {
                                            select("CREATE_SNIPPET_SCREEN")
                                        },
                                        onSnippetClick = { select(it.id) }
                                )
                )
            }
            detail { model ->
                model?.also { snippetUiModel ->
                    if (snippetUiModel == SnippetsViewModel.CreateSnippetItem) {
                        val createSnippetViewModel: CreateSnippetViewModel = koinViewModel()
                        CreateSnippetScreen(state = createSnippetViewModel.toState())
                        createSnippetViewModel.onSuccess.LaunchCollection {
                            select(null)
                            snippetsViewModel.refreshSnippets()
                        }
                    } else {
                        val snippetDetailsViewModel: SnippetDetailsViewModel = koinViewModel()
                        SnippetDetailsScreen(state = snippetDetailsViewModel.toState())
                        snippetDetailsViewModel.getSnippetDetails(snippetUiModel)
                    }
                }
                        ?: run { Box(modifier = Modifier.fillMaxSize().background(Color.Gray)) }
            }
            detailState { detailShowing ->
                snippetsViewModel.showCreateCta.value = !detailShowing
                mainWindowControls.topLevel =
                        !detailShowing || widthSize == WindowWidthSizeClass.Compact
                mainWindowControls.navIntercept =
                        if (detailShowing) {
                            ({
                                    select(null)
                                    true
                                })
                        } else {
                            null
                        }
            }
        }

        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    snippetsViewModel.refreshSnippets()
                }
            }
            lifecycle.addObserver(observer)
            onDispose { lifecycle.removeObserver(observer) }
        }
    }
}
