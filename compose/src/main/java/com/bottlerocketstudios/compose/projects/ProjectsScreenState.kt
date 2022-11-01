package com.bottlerocketstudios.compose.projects

import androidx.compose.runtime.State

data class ProjectsScreenState(
    val projectsList: State<List<ProjectsItemState>>,
)

data class ProjectsItemState(
    val name: State<String>,
    val key: State<String>,
    val updated: State<String>,
)
