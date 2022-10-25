package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.BranchDto
import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import java.time.ZonedDateTime

fun BranchDto.toBranch() = Branch(name = name.orEmpty(), date = target?.date ?: ZonedDateTime.now())
