package com.bottlerocketstudios.brarchitecture.test

import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogicImpl
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.ToasterImpl
import com.bottlerocketstudios.brarchitecture.test.mocks.MockBitBucketRepo
import com.bottlerocketstudios.brarchitecture.test.mocks.MockEnvironmentRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.testApplicationFactory
import com.bottlerocketstudios.brarchitecture.test.mocks.testContext
import org.koin.dsl.module
import java.time.Clock

object TestModule {
    // Default mocks can be overridden using inlineKoinSingle function
    fun generateMockedTestModule() = module {
        single { testContext }
        single { testApplicationFactory() }
        single<DispatcherProvider> { TestDispatcherProvider() }
        single { ForceCrashLogicImpl }
        single<Clock> { Clock.systemDefaultZone() }
        single<Toaster> { ToasterImpl(testContext) }
        single { MockBitBucketRepo.bitbucketRepository }
        single { MockEnvironmentRepository.mockEnvironmentRepository }
    }
}
