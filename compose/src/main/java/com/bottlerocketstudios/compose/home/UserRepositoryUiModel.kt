package com.bottlerocketstudios.compose.home

import com.bottlerocketstudios.brarchitecture.domain.models.Repository

data class UserRepositoryUiModel(
    val repo: Repository,
    var updatedTimeString: String? = null
)
