package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.WorkspaceDto
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace

fun WorkspaceDto.convertToWorkspace(): Workspace {
    return Workspace(
        slug = slug,
        name = name,
        uuid = uuid
    )
}
