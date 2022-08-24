package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.domain.models.Commit

fun CommitDto.toCommit() = Commit(
    hash = hash.orEmpty(),
    author = author?.toAuthor(),
    date = date,
    message = message.orEmpty()
)
