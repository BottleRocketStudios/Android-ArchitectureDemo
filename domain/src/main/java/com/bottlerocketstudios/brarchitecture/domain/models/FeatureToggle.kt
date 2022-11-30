package com.bottlerocketstudios.brarchitecture.domain.models

data class FeatureToggle(
    val name: String?,
    val value: Boolean?,
    val defaultValue: Boolean?,
    val requireRestart: Boolean?,
)
