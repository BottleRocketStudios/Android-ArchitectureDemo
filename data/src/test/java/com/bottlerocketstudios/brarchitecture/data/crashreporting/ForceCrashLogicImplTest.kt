package com.bottlerocketstudios.brarchitecture.data.crashreporting

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.data.test.mocks.MockBuildConfigProviders
import org.junit.Test

class ForceCrashLogicImplTest : BaseTest() {

    @Test(expected = Test.None::class /* no exception expected */)
    fun forceCrashOnMatch_nonMatchingInputReleaseBuild_doesNotThrowException() {
        val sut = ForceCrashLogicImpl(MockBuildConfigProviders.PROD_RELEASE)
        sut.forceCrashOnMatch("499240299839994204")
    }

    @Test(expected = RuntimeException::class)
    fun forceCrashOnMatch_matchingInputDevBuild_throwsException() {
        val sut = ForceCrashLogicImpl(MockBuildConfigProviders.DEV)
        sut.forceCrashOnMatch("crashthisapp!")
    }

    @Test(expected = RuntimeException::class)
    fun forceCrashOnMatch_matchingInputReleaseBuild_throwsException() {
        val sut = ForceCrashLogicImpl(MockBuildConfigProviders.PROD_RELEASE)
        sut.forceCrashOnMatch("crashthisapp!")
    }

    @Test(expected = RuntimeException::class)
    fun forceCrashNow_devBuild_throwsException() {
        val sut = ForceCrashLogicImpl(MockBuildConfigProviders.DEV)
        sut.forceCrashNow()
    }

    @Test(expected = Test.None::class /* no exception expected */)
    fun forceCrashNow_releaseBuild_doesNotThrowException() {
        val sut = ForceCrashLogicImpl(MockBuildConfigProviders.PROD_RELEASE)
        sut.forceCrashNow()
    }
}
