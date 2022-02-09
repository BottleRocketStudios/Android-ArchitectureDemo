package com.bottlerocketstudios.brarchitecture.test

import org.junit.Before
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
    @Before
    fun plantTimber() {
        Timber.plant(SystemOutPrintlnTree())
    }

    /** Used to declare a Koin single within a module and load immediately, overriding any definitions previously set. */
    inline fun <reified T> inlineKoinSingle(crossinline block: Scope.() -> T) {
        loadKoinModules(module { single { block() } })
    }
}
