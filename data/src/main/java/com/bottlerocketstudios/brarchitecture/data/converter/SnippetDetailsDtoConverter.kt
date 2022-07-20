package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.snippets.SnippetDetailsUiModel

fun SnippetDetailsDto.convertToUiModel() =
    SnippetDetailsUiModel(
        id = id,
        title = title,
        createdMessage = created?.convertToTimeAgoMessage(),
        updatedMessage = updated?.convertToTimeAgoMessage(),
        isPrivate = isPrivate,
        files = files?.convertToUiModel(),
        owner = owner?.convertToUser(),
        creator = creator?.convertToUser(),
        links = links?.convertToLinks()
    )

fun Map<String, LinksDto?>?.convertToUiModel(): List<SnippetDetailsFile?>? =
    this?.map { SnippetDetailsFile(it.key, it.value?.convertToLinks()) }





