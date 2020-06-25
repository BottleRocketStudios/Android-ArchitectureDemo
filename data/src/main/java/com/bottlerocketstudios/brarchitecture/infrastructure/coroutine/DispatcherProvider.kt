package com.bottlerocketstudios.brarchitecture.infrastructure.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstracts Dispatchers for better/easier testing!
 *
 * Dispatchers should always be injected. See https://medium.com/androiddevelopers/testing-two-consecutive-livedata-emissions-in-coroutines-5680b693cbf8
 */
interface DispatcherProvider {
    val Default: CoroutineDispatcher
    val IO: CoroutineDispatcher
    val Main: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}
