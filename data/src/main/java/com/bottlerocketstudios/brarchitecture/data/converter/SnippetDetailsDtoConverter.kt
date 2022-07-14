package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsFileDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.snippets.SnippetDetailsUiModel

fun SnippetDetailsDto.convertToUiModel() =
    SnippetDetailsUiModel(
        id = id,
        title = title,
        created = created,
        updated = updated,
        isPrivate = isPrivate,
        files = files?.map { it?.convertToUiModel() },
        owner = owner?.convertToUser(),
        creator = creator?.convertToUser()
    )


fun SnippetDetailsFileDto.convertToUiModel(): SnippetDetailsFile? {
    var snippetFile: SnippetDetailsFile? = null
        file?.forEach { map ->
            map.key.forEach { key ->
                snippetFile = SnippetDetailsFile(key.toString(), map.value.convertToLinks())
            }
        }
    return snippetFile
}



