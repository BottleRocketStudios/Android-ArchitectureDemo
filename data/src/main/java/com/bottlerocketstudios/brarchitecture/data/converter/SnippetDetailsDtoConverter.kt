package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFileLinks
import com.bottlerocketstudios.brarchitecture.domain.utils.convertToTimeAgoMessage
import com.bottlerocketstudios.compose.snippets.snippetDetails.SnippetDetailsUiModel

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
        links = links?.convertToLinks(),
        httpsCloneLink = links?.convertToLinks()?.clone?.find { it?.name == "https" }?.href,
        sshCloneLink = links?.convertToLinks()?.clone?.find { it?.name == "ssh" }?.href,
    )

fun Map<String, LinksDto>.convertToUiModel(): List<SnippetDetailsFileLinks> =
    map { SnippetDetailsFileLinks(it.key, it.value.convertToLinks()) }
