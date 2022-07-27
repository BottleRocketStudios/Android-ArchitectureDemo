package com.bottlerocketstudios.compose.test

import com.bottlerocketstudios.compose.test.mocks.testContext
import org.koin.dsl.module
import java.time.Clock

object TestModule {
    // Default mocks can be overridden using inlineKoinSingle function
    fun generateMockedTestModule() = module {
        single { testContext }
        single<Clock> { Clock.systemDefaultZone() }
    }
}
