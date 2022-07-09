package com.bottlerocketstudios.brarchitecture.test.mocks

import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.mock

object MockBuildConfigProvider {
    var _isDebugOrInternalBuild = MutableStateFlow(true)
    var _isProductionReleaseBuild = MutableStateFlow(false)
    private var _buildIdentifier: String = ""

    val buildConfigProvider: BuildConfigProvider = mock {
        on { isDebugOrInternalBuild }.then { _isDebugOrInternalBuild.value }
        on { isProductionReleaseBuild }.then { _isProductionReleaseBuild.value }
        on { buildIdentifier }.then { _buildIdentifier }

    }
}
