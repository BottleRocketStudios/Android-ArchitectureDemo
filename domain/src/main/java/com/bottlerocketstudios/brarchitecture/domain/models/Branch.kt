package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class Branch(
    val name: String,
    val date: ZonedDateTime,
)
