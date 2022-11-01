package com.bottlerocketstudios.brarchitecture.ui.projects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.projects.ProjectsScreenState

@Composable
fun ProjectsViewModel.toState() = ProjectsScreenState(
    projectsList = projectsList.collectAsState(initial = emptyList())
)
