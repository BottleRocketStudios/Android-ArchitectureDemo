package com.bottlerocketstudios.brarchitecture.test

import org.junit.Before
import org.koin.core.context.loadKoinModules
import org.koin.core.scope.Scope
import org.koin.dsl.module
import timber.log.Timber

open class BaseTest {
    @Before
    fun plantTimber() {
        Timber.plant(SystemOutPrintlnTree())
    }

    /** Used to declare a Koin single within a module and load immediately */
    inline fun <reified T> inlineKoinSingle(override: Boolean = false, crossinline block: Scope.() -> T) {
        loadKoinModules(module(override = override) { single { block() } })
    }
}
