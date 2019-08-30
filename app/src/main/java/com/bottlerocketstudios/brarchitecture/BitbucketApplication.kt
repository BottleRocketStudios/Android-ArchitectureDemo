package com.bottlerocketstudios.brarchitecture

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import timber.log.Timber
import java.lang.reflect.InvocationTargetException


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