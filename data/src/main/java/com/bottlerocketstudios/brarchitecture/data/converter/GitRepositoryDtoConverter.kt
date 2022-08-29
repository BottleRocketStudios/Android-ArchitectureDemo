package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository

fun GitRepositoryDto.convertToGitRepository() = GitRepository(
    scm = scm,
    name = name,
    owner = owner?.toUser(),
    workspace = workspaceDto?.convertToWorkspace(),
    isPrivate = isPrivate,
    description = description,
    updated = updated
)
