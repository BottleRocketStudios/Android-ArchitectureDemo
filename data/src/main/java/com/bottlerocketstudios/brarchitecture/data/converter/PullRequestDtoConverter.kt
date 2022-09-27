package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.PullRequestDto
import com.bottlerocketstudios.brarchitecture.domain.models.PullRequest

fun PullRequestDto.toPullRequest() = PullRequest(
    title = title.orEmpty(),
    state = state.orEmpty(),
    createdOn = createdOn,
    author = author?.displayName.orEmpty(),
    source = source?.branch?.name.orEmpty(),
    destination = destination?.branch?.name.orEmpty()
)
