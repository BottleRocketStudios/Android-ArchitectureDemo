package com.bottlerocketstudios.brarchitecture.buildconfig

import com.bottlerocketstudios.brarchitecture.BuildConfig
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider

class BuildConfigProviderImpl : BuildConfigProvider {
    override val isDebugOrInternalBuild: Boolean
        get() = isDebugOrInternalBuild()
    override val isProductionReleaseBuild: Boolean
        get() = isProductionReleaseBuild()
    override val buildIdentifier: String
        get() = BuildConfig.BUILD_IDENTIFIER
}

private fun isProductionReleaseBuild() = !BuildConfig.DEBUG

private fun isDebugOrInternalBuild() = BuildConfig.DEBUG
