package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.MainWindowControls
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.repository.RepositoryCommitScreen
import com.bottlerocketstudios.launchpad.compose.util.LaunchCollection
import org.koin.androidx.compose.getViewModel

fun NavGraphBuilder.repositoryCommitsComposable(controls: MainWindowControls) {
    composable(Routes.Commits) {
        val viewModel: RepositoryCommitViewModel = getViewModel()
        val activityViewModel: ComposeActivityViewModel = getViewModel()

        val repoName = activityViewModel.selectedRepo.value.name ?: ""
        RepositoryCommitScreen(state = viewModel.toState())

        // Update top level controls
        controls.reset()
        controls.title = repoName
        controls.topLevel = true

        activityViewModel.selectedRepo.LaunchCollection(collector = {
            viewModel.currentRepoName.value = it.name.orEmpty()
        })
    }
}
