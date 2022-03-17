package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class Repository(
    val scm: String?,
    val name: String?,
    val owner: User?,
    val workspace: Workspace?,
    val isPrivate: Boolean? = true,
    val description: String?,
    val updated: ZonedDateTime?
)
