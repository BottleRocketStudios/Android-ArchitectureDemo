package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.domain.models.Snippet

fun SnippetDto.convertToSnippet() = Snippet(
    id = id,
    title = title,
    isPrivate = isPrivate,
    owner = owner?.convertToUser(),
    updated = updated,
)
