package com.bottlerocketstudios.compose.home

import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.compose.util.StringIdHelper

data class UserRepositoryUiModel(
    val repo: GitRepository,
    val formattedLastUpdatedTime: StringIdHelper,
)
