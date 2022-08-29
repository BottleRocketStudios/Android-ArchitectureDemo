package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFileLinks
import com.bottlerocketstudios.brarchitecture.domain.utils.convertToTimeAgoMessage

fun SnippetDetailsDto.toSnippetDetails() =
    SnippetDetails(
        id = id,
        title = title,
        createdMessage = created?.convertToTimeAgoMessage(),
        updatedMessage = updated?.convertToTimeAgoMessage(),
        isPrivate = isPrivate,
        files = files?.toSnippetDetails(),
        owner = owner?.toUser(),
        creator = creator?.toUser(),
        links = links?.toLinks(),
        httpsCloneLink = links?.toLinks()?.clone?.find { it?.name == "https" }?.href,
        sshCloneLink = links?.toLinks()?.clone?.find { it?.name == "ssh" }?.href,
    )

fun Map<String, LinksDto>.toSnippetDetails(): List<SnippetDetailsFileLinks> =
    map { SnippetDetailsFileLinks(it.key, it.value.toLinks()) }
