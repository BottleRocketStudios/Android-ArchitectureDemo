package com.bottlerocketstudios.brarchitecture.test

import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.test.mocks.testContext
import org.koin.dsl.module
import java.time.Clock

object TestModule {
    // Default mocks can be overridden using inlineKoinSingle function
    fun generateMockedTestModule() = module {
        single { testContext }
        single<DispatcherProvider> { TestDispatcherProvider() }
        single<Clock> { Clock.systemDefaultZone() }
    }
}
