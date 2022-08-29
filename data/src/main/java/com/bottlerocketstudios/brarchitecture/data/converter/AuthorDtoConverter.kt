package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.AuthorDto
import com.bottlerocketstudios.brarchitecture.domain.models.Author

fun AuthorDto.toAuthor() = Author(
    user = userInfo?.toUser()
)
