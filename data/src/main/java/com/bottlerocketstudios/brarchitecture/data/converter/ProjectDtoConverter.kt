package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.ProjectDto
import com.bottlerocketstudios.brarchitecture.domain.models.Project

fun ProjectDto.toProject() = Project(
    name = name.orEmpty(),
    key = key.orEmpty(),
    updatedOn = updated
)
