package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.domain.models.User

fun UserDto.convertToUser() = User(
    username = username,
    nickname = nickname,
    accountStatus = accountStatus,
    displayName = displayName,
    createdOn = createdOn,
    uuid = uuid,
    links = linksDto?.convertToLinks(),
    avatarUrl = linksDto?.convertToLinks()?.avatar?.href
)
