package com.bottlerocketstudios.brarchitecture.startup

import android.content.Context
import androidx.startup.Initializer
import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import timber.log.Timber

/** AndroidX Startup Timber initializer */
class TimberStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        // Can't use Koin to create this due to necessary logic needed in startKoin for androidLogger. Just create/use an instance here for this special case.
        val buildConfigProvider = BuildConfigProviderImpl()
        if (buildConfigProvider.isDebugOrInternalBuild) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.v("[create]")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
