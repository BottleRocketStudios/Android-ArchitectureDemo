package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.RepoFileDto
import com.bottlerocketstudios.brarchitecture.domain.models.RepoFile

fun RepoFileDto.toRepoFile() = RepoFile(
    type = type.orEmpty(),
    path = path.orEmpty(),
    mimeType = mimetype.orEmpty(),
    size = size ?: 0,
    commit = commit?.toCommit(),
)
