package com.bottlerocketstudios.brarchitecture.test

import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestDispatcherProvider(coroutineDispatcher: CoroutineDispatcher) : DispatcherProvider {
    override val Default: CoroutineDispatcher = coroutineDispatcher
    override val IO: CoroutineDispatcher = coroutineDispatcher
    override val Main: CoroutineDispatcher = coroutineDispatcher
    override val Unconfined: CoroutineDispatcher = coroutineDispatcher
}

fun TestDispatcher.generateTestDispatcherProvider() = TestDispatcherProvider(this)
