package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinkDto
import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links

fun LinksDto.convertToLinks() = Links(avatar = avatar?.convertToLink())

fun LinkDto.convertToLink() = Link(href = href)