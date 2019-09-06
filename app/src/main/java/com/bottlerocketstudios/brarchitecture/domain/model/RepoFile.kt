package com.bottlerocketstudios.brarchitecture.domain.model


data class RepoFile(
    val type: String?,
    val path: String?,
    val mimetype: String?,
    val attributes: List<String>?,
    val size: Int,
    val commit: Commit
)