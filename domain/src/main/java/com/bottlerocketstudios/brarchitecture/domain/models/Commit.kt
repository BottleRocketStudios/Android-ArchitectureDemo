package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class Commit(
    val hash: String,
    val message: String,
    val author: Author?,
    val date: ZonedDateTime?,
)
