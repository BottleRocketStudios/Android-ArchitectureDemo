package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.repository.RepositoryCommitScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun ComposeActivity.repositoryCommitsComposable(navGraphBuilder: NavGraphBuilder) {
    val viewModel: RepositoryCommitViewModel = getViewModel()
    val activityViewModel: ComposeActivityViewModel = getViewModel()

    navGraphBuilder.composable(
        Routes.Commits
    ) {
        val repoName = activityViewModel.selectedRepo.value.name ?: ""
        viewModel.ConnectBaseViewModel {
            RepositoryCommitScreen(state = it.toState())
        }

        controls.title = repoName
        controls.topLevel = true
    }
    viewModel.currentRepoName.value = activityViewModel.selectedRepo.value.name ?: ""
}
