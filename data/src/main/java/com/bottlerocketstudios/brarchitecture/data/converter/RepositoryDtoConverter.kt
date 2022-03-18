package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.RepositoryDto
import com.bottlerocketstudios.brarchitecture.domain.models.Repository

fun RepositoryDto.convertToRepository() = Repository(
    scm = scm,
    name = name,
    owner = owner?.convertToUser(),
    workspace = workspaceDto?.convertToWorkspace(),
    isPrivate = isPrivate,
    description = description,
    updated = updated
)
