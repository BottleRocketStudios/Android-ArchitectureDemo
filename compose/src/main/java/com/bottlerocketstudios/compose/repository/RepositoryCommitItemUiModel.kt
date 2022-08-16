package com.bottlerocketstudios.compose.repository

import com.bottlerocketstudios.compose.util.StringIdHelper

data class RepositoryCommitItemUiModel(
    val author: String,
    val timeSinceCommitted: StringIdHelper,
    val hash: String,
    val message: String,
    val branchName: String
)
