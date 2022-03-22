package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.repository.FileBrowserScreenState

@Composable
fun RepositoryFileFragmentViewModel.toState() = FileBrowserScreenState(
    path = path.collectAsState(),
    content = srcFile.collectAsState()
)
