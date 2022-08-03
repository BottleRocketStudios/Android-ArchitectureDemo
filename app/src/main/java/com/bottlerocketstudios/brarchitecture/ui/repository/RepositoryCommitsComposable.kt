package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.compose.repository.RepositoryCommitScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun ComposeActivity.repositoryCommitsComposable(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(
        "commits"
    ) {
        val activityViewModel: ComposeActivityViewModel = getViewModel()
        val viewModel: RepositoryCommitViewModel = getViewModel()
        val repoName = activityViewModel.selectedRepo.value.name ?: ""
        viewModel.ConnectBaseViewModel {
            RepositoryCommitScreen(state = it.toState())
            it.getRepoCommits(repoName)
        }

        controls.title = repoName
        controls.topLevel = true
    }
}
