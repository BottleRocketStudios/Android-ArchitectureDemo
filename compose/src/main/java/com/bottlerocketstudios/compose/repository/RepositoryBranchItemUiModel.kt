package com.bottlerocketstudios.compose.repository

import com.bottlerocketstudios.compose.util.StringIdHelper

data class RepositoryBranchItemUiModel(
    val name: String,
    val timeSinceCreated: StringIdHelper,
    val status: StringIdHelper,
)
