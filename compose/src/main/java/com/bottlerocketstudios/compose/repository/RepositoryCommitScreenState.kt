package com.bottlerocketstudios.compose.repository

import androidx.compose.runtime.State

data class RepositoryCommitScreenState(
    val path: State<String>,
    val itemCount: State<Int>,
    val commitItems: State<List<RepositoryCommitItemUiModel>>
)
