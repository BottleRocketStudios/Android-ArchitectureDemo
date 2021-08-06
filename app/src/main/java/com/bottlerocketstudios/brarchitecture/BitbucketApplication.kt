package com.bottlerocketstudios.brarchitecture

import android.app.Application
import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.jakewharton.processphoenix.ProcessPhoenix
import timber.log.Timber

// TODO: TEMPLATE - Refactor this class's name when creating a new project.
@Suppress("unused")
class BitbucketApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) {
            // skip initialization when in the Phoenix process (from environment switcher)
            return
        }
        // Can't use Koin to create this due to necessary logic needed in startKoin for androidLogger. Just create/use an instance here for this special case.
        val buildConfigProvider = BuildConfigProviderImpl()
        if (buildConfigProvider.isDebugOrInternalBuild) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.v("[onCreate]")
        // See startup.* classes for additional androidx app startup initializers
    }
}
