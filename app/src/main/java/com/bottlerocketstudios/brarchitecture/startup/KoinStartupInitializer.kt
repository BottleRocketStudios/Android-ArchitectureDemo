package com.bottlerocketstudios.brarchitecture.startup

import android.content.Context
import androidx.startup.Initializer
import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.di.Data
import com.bottlerocketstudios.brarchitecture.data.di.NetworkObject
import com.bottlerocketstudios.brarchitecture.data.di.TokenAuth
import com.bottlerocketstudios.brarchitecture.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import timber.log.Timber

/** AndroidX Startup Koin initializer */
class KoinStartupInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        Timber.v("[create]")
        // Can't use Koin to create this due to necessary logic needed in startKoin for androidLogger. Just create/use an instance here for this special case.
        val buildConfigProvider = BuildConfigProviderImpl()
        return startKoin {
            if (buildConfigProvider.isDebugOrInternalBuild) {
                androidLogger()
            }

            androidContext(context)
            modules(
                listOf(
                    AppModule.appModule,
                    Data.dataModule,
                    TokenAuth.tokenAuthModule,
                    NetworkObject.networkModule
                )
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(TimberStartupInitializer::class.java)
}
