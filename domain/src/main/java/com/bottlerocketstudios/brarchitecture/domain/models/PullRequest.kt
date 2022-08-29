package com.bottlerocketstudios.brarchitecture.domain.models

import java.time.ZonedDateTime

data class PullRequest(
    val title: String,
    val state: String,
    val createdOn: ZonedDateTime?,
)