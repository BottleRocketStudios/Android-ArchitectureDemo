package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinkDto
import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links

fun LinksDto.toLinks() = Links(
    self = self?.convertToLink(),
    html = html?.convertToLink(),
    comments = comments?.convertToLink(),
    watchers = watchers?.convertToLink(),
    commits = commits?.convertToLink(),
    diff = diff?.convertToLink(),
    clone = clone?.map { it?.convertToLink() },
    patch = patch?.convertToLink(),
    avatar = avatar?.convertToLink(),
    followers = followers?.convertToLink(),
    following = following?.convertToLink(),
    repositories = repositories?.convertToLink()
)

fun LinkDto.convertToLink() = Link(href = href, name = name)
