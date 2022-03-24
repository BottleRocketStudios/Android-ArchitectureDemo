package com.bottlerocketstudios.brarchitecture.domain.models

import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentType

/** Contains all environment specific data. Globally unique data should live outside of this config. */
data class EnvironmentConfig(
    val environmentType: EnvironmentType,
    /** Base url for the environment */
    val baseUrl: String,
) : DomainModel
