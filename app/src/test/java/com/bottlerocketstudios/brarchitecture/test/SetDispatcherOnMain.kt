package com.bottlerocketstudios.brarchitecture.test

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class SetDispatcherOnMain(private val dispatcher: CoroutineDispatcher) : TestWatcher() {
    override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)
    override fun finished(description: Description?) = Dispatchers.resetMain()
}
