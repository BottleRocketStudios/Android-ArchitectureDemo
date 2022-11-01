package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class Project(
    val name: String,
    val key: String,
    val updatedOn: ZonedDateTime?,
)
