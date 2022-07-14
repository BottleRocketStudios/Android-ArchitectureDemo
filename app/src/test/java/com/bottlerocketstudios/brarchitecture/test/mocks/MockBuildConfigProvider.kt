package com.bottlerocketstudios.brarchitecture.test.mocks

import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider

const val TEST_BUILD_DEV = "test_build_dev"
const val TEST_BUILD_PROD = "test_build_prod"

/** Preconfigured [BuildConfigProvider] to simplify usage of common configurations for easier testing  */
object MockBuildConfigProvider {
    val DEV = object : BuildConfigProvider {
        override val isDebugOrInternalBuild = true
        override val isProductionReleaseBuild = !isDebugOrInternalBuild
        override val buildIdentifier = TEST_BUILD_DEV
    }

    val PROD_RELEASE = object : BuildConfigProvider {
        override val isDebugOrInternalBuild = false
        override val isProductionReleaseBuild = !isDebugOrInternalBuild
        override val buildIdentifier = TEST_BUILD_PROD
    }
}
