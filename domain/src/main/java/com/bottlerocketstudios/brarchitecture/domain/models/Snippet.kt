package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class Snippet(
    val id: String?,
    val title: String?,
    val isPrivate: Boolean?,
    val owner: User?,
    val updated: ZonedDateTime?,
    val workspace: Workspace?,
) : DomainModel
