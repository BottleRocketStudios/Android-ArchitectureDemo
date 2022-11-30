package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleDto
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle

fun FeatureToggleDto.toFeatureToggle() = FeatureToggle(
    name = name.orEmpty(),
    value = value,
    defaultValue = defaultValue,
    requireRestart = requireRestart
)
