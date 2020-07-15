package com.bottlerocketstudios.brarchitecture

import android.app.Application
import com.bottlerocketstudios.brarchitecture.di.AppModule
import com.bottlerocketstudios.brarchitecture.data.di.Data
import com.bottlerocketstudios.brarchitecture.data.di.NetworkObject
import com.bottlerocketstudios.brarchitecture.data.di.TokenAuth
import com.jakewharton.processphoenix.ProcessPhoenix
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class BitbucketApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) {
            // skip initialization when in the Phoenix process (from environment switcher)
            return
        }

        // TODO: Remove logging in production
        Timber.plant(Timber.DebugTree())
        startKoin {
            // TODO: Remove logging in production
            androidLogger()
            androidContext(this@BitbucketApplication)
            modules(listOf(
                AppModule.appModule,
                Data.dataModule,
                TokenAuth.tokenAuthModule,
                NetworkObject.networkModule))
        }
    }
}
