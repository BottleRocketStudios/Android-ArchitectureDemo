package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedViewModel(app: Application) : BaseViewModel(app), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
            get() = job + context
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    companion object {
        var context = Dispatchers.IO
    }
}
