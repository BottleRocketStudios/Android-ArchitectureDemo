package com.bottlerocketstudios.brarchitecture.test.mocks

import com.bottlerocketstudios.brarchitecture.ui.devoptions.ApplicationInfo
import com.bottlerocketstudios.brarchitecture.ui.devoptions.ApplicationInfoManager
import org.mockito.kotlin.mock

const val TEST_VERSION_NAME = "test_version_name"
const val TEST_VERSION_CODE = 1
const val TEST_PACKAGE_NAME = "test_package_name"

object MockApplicationInfoManager {
    var appInfo: ApplicationInfo = ApplicationInfo(
        appVersionName = TEST_VERSION_NAME,
        appVersionCode = TEST_VERSION_CODE.toString(),
        appId = TEST_PACKAGE_NAME,
        buildIdentifier = TEST_BUILD_DEV,
    )

    @Suppress("MemberNameEqualsClassName")
    val mockApplicationInfoManager: ApplicationInfoManager = mock {
        on { getApplicationInfo() }.then { appInfo }
    }
}
