package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.BranchDto
import com.bottlerocketstudios.brarchitecture.domain.models.Branch

fun BranchDto.toBranch() = Branch(
    name = name.orEmpty(),
)
