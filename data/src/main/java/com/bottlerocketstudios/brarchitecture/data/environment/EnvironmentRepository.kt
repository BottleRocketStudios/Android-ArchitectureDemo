package com.bottlerocketstudios.brarchitecture.data.environment

import com.bottlerocketstudios.brarchitecture.data.model.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.domain.models.RepositoryInterface

/**
 * Provides client environment related values.
 */
interface EnvironmentRepository : RepositoryInterface {
    /** Currently selected environment. Always production data on production release build. */
    val selectedConfig: EnvironmentConfig

    /** List of environments. Only contains production on production release build. */
    val environments: List<EnvironmentConfig>

    /** Modifies the current environment to [environmentType]. No-op if called on production release build. */
    fun changeEnvironment(environmentType: EnvironmentType)
}
