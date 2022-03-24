package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class Snippet(
    val id: String? = null,
    val title: String? = null,
    val isPrivate: Boolean? = null,
    val owner: User? = null,
    val updated: ZonedDateTime? = null,
)
