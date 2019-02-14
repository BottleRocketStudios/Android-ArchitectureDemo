package com.bottlerocketstudios.brarchitecture

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import timber.log.Timber

open class BaseTest {
    @Before
    fun plantTimber() {
        Timber.plant(SystemOutPrintlnTree())
    }
}
