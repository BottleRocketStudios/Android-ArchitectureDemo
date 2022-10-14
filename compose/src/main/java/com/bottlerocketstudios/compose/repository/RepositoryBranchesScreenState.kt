package com.bottlerocketstudios.compose.repository

import androidx.compose.runtime.State

data class RepositoryBranchesScreenState(
    val path: State<String>,
    val itemCount: State<Int>,
    val branchItems: State<List<RepositoryBranchItemUiModel>>,
    var search: String,
    var filteredList: List<RepositoryBranchItemUiModel>
)
