package com.bottlerocketstudios.brarchitecture.startup

import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

/** AndroidX Startup primary initializer (used to create a single AndroidManifest entrypoint) */
class AppStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.v("[create]")
        // no-op
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        TimberStartupInitializer::class.java,
        KoinStartupInitializer::class.java,
    )
}
