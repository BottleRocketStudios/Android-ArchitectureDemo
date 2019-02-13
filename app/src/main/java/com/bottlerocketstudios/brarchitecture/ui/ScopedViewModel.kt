package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class ScopedViewModel(app: Application) : BaseViewModel(app), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext 
            get() = job + Dispatchers.IO

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
