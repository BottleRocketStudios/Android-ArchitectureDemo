package com.bottlerocketstudios.compose.repository

data class RepositoryItemUiModel(
    val path: String,
    val size: Int,
    val isFolder: Boolean,
)
