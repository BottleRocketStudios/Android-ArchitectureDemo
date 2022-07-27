package com.bottlerocketstudios.brarchitecture

import android.app.Application
import timber.log.Timber

// TODO: TEMPLATE - Refactor this class's name when creating a new project.
/** Custom Application class. See startup.* classes for additional androidx app startup initializers (ex: [com.bottlerocketstudios.brarchitecture.startup.AppStartupInitializer]) */
class BitbucketApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.v("[onCreate]") // timber should be initialized by this point
    }
}
