package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleBooleanDto
import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleStringDto
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle

fun FeatureToggleBooleanDto.toFeatureToggle(): FeatureToggle {
    return FeatureToggle.ToggleValueBoolean(
        name = name,
        value = value,
        defaultValue = defaultValue,
        requireRestart = requireRestart
    )
}

fun FeatureToggleStringDto.toFeatureToggle(): FeatureToggle {
    return FeatureToggle.ToggleValueEnum(
        name = name,
        value = FeatureToggle.ToggleValueEnum.ToggleEnum.valueOf(value),
        defaultValue = FeatureToggle.ToggleValueEnum.ToggleEnum.valueOf(defaultValue),
        requireRestart = requireRestart
    )
}
