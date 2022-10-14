package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.repository.RepositoryBranchesScreenState

@Composable
fun RepositoryBranchesViewModel.toState() = RepositoryBranchesScreenState(
    path = path.collectAsState(),
    itemCount = itemCount.collectAsState(),
    branchItems = uiModels.collectAsState(),
    search = "",
    filteredList = emptyList()
)
