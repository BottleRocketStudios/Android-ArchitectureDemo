package com.bottlerocketstudios.brarchitecture.ui.projects

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.projects.ProjectsScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun ComposeActivity.projectsComposable(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(Routes.Projects) {
        val vm: ProjectsViewModel = getViewModel()
        vm.ConnectBaseViewModel {
            ProjectsScreen(state = it.toState())
        }

        controls.title = stringResource(id = R.string.projects)
        controls.topLevel = true
    }
}
