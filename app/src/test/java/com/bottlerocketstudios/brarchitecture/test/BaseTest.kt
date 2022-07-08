package com.bottlerocketstudios.brarchitecture.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.core.context.loadKoinModules
import org.koin.core.scope.Scope
import org.koin.dsl.module
import timber.log.Timber

/**
 * Common setup/initialization all JUnit tests should inherit from.
 *
 * ### Test Function Naming
 * All test methods names should follow the template below:
 *
 * `methodNameUnderTest_conditionBeingTested_expectedResult`
 *
 * ### Test Function Body
 * 1. Use the **Arrange, Act, Assert** pattern.
 * 2. Blank lines should indicate the different steps in the pattern (comments denoting each section should not be used).
 *
 * Additional resources:
 * * https://wiki.c2.com/?ArrangeActAssert
 * * https://xp123.com/articles/3a-arrange-act-assert/
 * * https://medium.com/swlh/an-experience-of-unit-testing-with-the-arrange-act-assert-aaa-pattern-part-i-53babd01c52b
 */
open class BaseTest {
    val testDispatcherProvider = TestDispatcherProvider()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val koinRule = KoinTestRule(TestModule.generateMockedTestModule())

    @Before
    fun plantTimber() {
        Timber.plant(SystemOutPrintlnTree())
    }

    @Before
    fun setMainDispatchers() {
        Dispatchers.setMain(testDispatcherProvider.Unconfined)
    }

    @After
    fun resetDispatchers() {
        Dispatchers.resetMain()
    }

    /** Used to declare a Koin single within a module and load immediately, overriding any definitions previously set. */
    inline fun <reified T> inlineKoinSingle(crossinline block: Scope.() -> T) {
        loadKoinModules(module { single { block() } })
    }
}
