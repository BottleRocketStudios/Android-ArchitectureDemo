package com.bottlerocketstudios.compose.repository

import androidx.annotation.DrawableRes

data class RepositoryItemUiModel(
    @DrawableRes val icon: Int,
    val path: String,
    val size: String,
    val isFolder: Boolean
)
