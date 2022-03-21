package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class GitRepository(
    val scm: String?,
    val name: String?,
    val owner: User?,
    val workspace: Workspace?,
    val isPrivate: Boolean?,
    val description: String?,
    val updated: ZonedDateTime?
)
