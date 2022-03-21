package com.bottlerocketstudios.compose.home

import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository

data class UserRepositoryUiModel(
    val repo: GitRepository,
    var updatedTimeString: String? = null
)
