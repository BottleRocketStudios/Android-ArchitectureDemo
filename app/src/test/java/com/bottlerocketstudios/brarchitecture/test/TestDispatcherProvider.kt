package com.bottlerocketstudios.brarchitecture.test

import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {
    override val Default: CoroutineDispatcher = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
    override val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
