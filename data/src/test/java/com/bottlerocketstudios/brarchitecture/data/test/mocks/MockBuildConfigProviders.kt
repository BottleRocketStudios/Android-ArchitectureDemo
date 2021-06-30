package com.bottlerocketstudios.brarchitecture.data.test.mocks

import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider

/** Preconfigured [BuildConfigProvider] to simplify usage of common configurations for easier testing  */
object MockBuildConfigProviders {
    val DEV = object : BuildConfigProvider {
        override val isDebugOrInternalBuild = true
        override val isProductionReleaseBuild = !isDebugOrInternalBuild
        override val buildIdentifier = ""
    }

    val PROD_RELEASE = object : BuildConfigProvider {
        override val isDebugOrInternalBuild = false
        override val isProductionReleaseBuild = !isDebugOrInternalBuild
        override val buildIdentifier = ""
    }
}
