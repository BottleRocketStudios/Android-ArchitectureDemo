package com.bottlerocketstudios.brarchitecture.test

import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestDispatcherProvider(coroutineDispatcher: CoroutineDispatcher) : DispatcherProvider {
    @Suppress("PropertyName")
    override val Default: CoroutineDispatcher = coroutineDispatcher

    @Suppress("PropertyName")
    override val IO: CoroutineDispatcher = coroutineDispatcher

    @Suppress("PropertyName")
    override val Main: CoroutineDispatcher = coroutineDispatcher

    @Suppress("PropertyName")
    override val Unconfined: CoroutineDispatcher = coroutineDispatcher
}

fun TestDispatcher.generateTestDispatcherProvider() = TestDispatcherProvider(this)
