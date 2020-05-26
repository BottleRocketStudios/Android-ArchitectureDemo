package com.bottlerocketstudios.brarchitecture

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class BitbucketApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@BitbucketApplication)
            modules(appModule)
        }
    }
}
