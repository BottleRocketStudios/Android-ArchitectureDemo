package com.bottlerocketstudios.brarchitecture.data.environment

import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentType
import com.bottlerocketstudios.brarchitecture.domain.models.Repository

/**
 * Provides client environment related values.
 */
interface EnvironmentRepository : Repository {
    /** Currently selected environment. Always production data on production release build. */
    val selectedConfig: EnvironmentConfig

    /** List of environments. Only contains production on production release build. */
    val environments: List<EnvironmentConfig>

    /** Modifies the current environment to [environmentType]. No-op if called on production release build. */
    fun changeEnvironment(environmentType: EnvironmentType)
}
