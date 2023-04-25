package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.MainWindowControls
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.repository.RepositoryBranchesScreen
import com.bottlerocketstudios.launchpad.compose.util.LaunchCollection
import org.koin.androidx.compose.getViewModel

fun NavGraphBuilder.repositoryBranchesComposable(controls: MainWindowControls) {
    composable(Routes.Branches) {
        val viewModel: RepositoryBranchesViewModel = getViewModel()
        val activityViewModel: ComposeActivityViewModel = getViewModel()

        // Update top level controls
        controls.reset()
        controls.title = activityViewModel.selectedRepo.value.name ?: ""
        controls.topLevel = true

        RepositoryBranchesScreen(state = viewModel.toState())

        activityViewModel.selectedRepo.LaunchCollection(collector = {
            viewModel.currentRepoName.value = it.name.orEmpty()
        })
    }
}
