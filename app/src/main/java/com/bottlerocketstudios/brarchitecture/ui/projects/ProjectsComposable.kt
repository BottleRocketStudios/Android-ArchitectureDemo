package com.bottlerocketstudios.brarchitecture.ui.projects

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.MainWindowControls
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.projects.ProjectsScreen
import org.koin.androidx.compose.getViewModel

fun NavGraphBuilder.projectsComposable(controls: MainWindowControls) {
    composable(Routes.Projects) {
        val vm: ProjectsViewModel = getViewModel()
        ProjectsScreen(state = vm.toState())

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.projects)
        controls.topLevel = true
    }
}
