package com.bottlerocketstudios.brarchitecture.domain.models

data class RepoFile(
    val type: String,
    val path: String,
    val mimeType: String,
    val size: Int,
    val commit: Commit?,
)
