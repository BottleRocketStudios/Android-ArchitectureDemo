package com.bottlerocketstudios.brarchitecture.startup

import android.content.Context
import androidx.startup.Initializer
import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.di.DataModule
import com.bottlerocketstudios.brarchitecture.data.di.NetworkModule
import com.bottlerocketstudios.brarchitecture.data.di.TokenAuthModule
import com.bottlerocketstudios.brarchitecture.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/** AndroidX Startup Koin initializer */
class KoinStartupInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        Timber.v("[create]")
        // Can't use Koin to create this due to necessary logic needed in startKoin for androidLogger. Just create/use an instance here for this special case.
        val buildConfigProvider = BuildConfigProviderImpl()
        return startKoin {
            if (buildConfigProvider.isDebugOrInternalBuild) {
                androidLogger(Level.ERROR) // FIXME: Change to back to INFO when koin 3.2.0 is released: https://github.com/InsertKoinIO/koin/issues/1188
            } else {
                androidLogger(Level.NONE)
            }

            androidContext(context)
            modules(
                listOf(
                    AppModule.module,
                    DataModule.module,
                    TokenAuthModule.module,
                    NetworkModule.module,
                )
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(TimberStartupInitializer::class.java)
}
