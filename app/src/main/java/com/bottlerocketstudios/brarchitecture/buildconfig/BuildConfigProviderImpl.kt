package com.bottlerocketstudios.brarchitecture.buildconfig

import com.bottlerocketstudios.brarchitecture.BuildConfig
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider

class BuildConfigProviderImpl : BuildConfigProvider {
    override val isDebugBuild: Boolean
        get() = isDebugBuild()
    override val isProductionReleaseBuild: Boolean
        get() = isProductionReleaseBuild()
    override val buildIdentifier: String
        get() = BuildConfig.BUILD_IDENTIFIER
}

private fun isProductionReleaseBuild() = !BuildConfig.DEBUG

private fun isDebugBuild() = BuildConfig.DEBUG
