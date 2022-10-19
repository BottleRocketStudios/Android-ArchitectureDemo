package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.repository.RepositoryBranchesScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun ComposeActivity.repositoryBranchesComposable(navGraphBuilder: NavGraphBuilder) {
    val viewModel: RepositoryBranchesViewModel = getViewModel()
    val activityViewModel: ComposeActivityViewModel = getViewModel()

    navGraphBuilder.composable(
        Routes.Branches
    ) {
        val repoName = activityViewModel.selectedRepo.value.name ?: ""
        viewModel.ConnectBaseViewModel {
            RepositoryBranchesScreen(state = it.toState())
        }

        controls.title = repoName
        controls.topLevel = true
    }
    viewModel.currentRepoName.value = activityViewModel.selectedRepo.value.name ?: ""
}
