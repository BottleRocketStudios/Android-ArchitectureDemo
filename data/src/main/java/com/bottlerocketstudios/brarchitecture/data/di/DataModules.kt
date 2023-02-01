package com.bottlerocketstudios.brarchitecture.data.di

import android.content.Context
import android.content.SharedPreferences
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogicImpl
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapper
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapperImpl
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketServiceFactory
import com.bottlerocketstudios.brarchitecture.data.network.TokenAuthServiceFactory
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.network.auth.basic.BasicAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.repository.FeatureToggleRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.serialization.DateTimeAdapter
import com.bottlerocketstudios.brarchitecture.data.serialization.ProtectedPropertyAdapter
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProviderImpl
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.time.Clock

/** General app configuration (repositories/viewmodels/etc) */
object DataModule {
    val module = module {
        // Clock for injectable time that can be replaced in tests
        single<Clock> { Clock.systemDefaultZone() }
        single<DispatcherProvider> { DispatcherProviderImpl() }
        single<Moshi> { Moshi.Builder().add(DateTimeAdapter(clock = get())).add(ProtectedPropertyAdapter()).build() }
        single<BitbucketRepository> { BitbucketRepositoryImpl() }
        single<FeatureToggleRepository> { FeatureToggleRepositoryImpl(moshi = get()) }
        single<EnvironmentRepository> { EnvironmentRepositoryImpl(sharedPrefs = get(named(KoinNamedSharedPreferences.Environment)), buildConfigProvider = get()) }
        single<ForceCrashLogic> { ForceCrashLogicImpl(buildConfigProvider = get()) }
        single { BitbucketCredentialsRepository(context = androidContext(), moshi = get()) }
        single<ResponseToApiResultMapper> { ResponseToApiResultMapperImpl() }
        single<SharedPreferences>(named(KoinNamedSharedPreferences.Environment)) {
            androidContext().getSharedPreferences("dev_options_prefs", Context.MODE_PRIVATE)
        }
        single { Firebase.remoteConfig }
    }
}

/** Allows multiple types of [SharedPreferences] to co-exist in the koin graph */
enum class KoinNamedSharedPreferences {
    Environment
}

/** Allows multiple types of network related implementations to co-exist in the koin graph */
private enum class KoinNamedNetwork {
    Unauthenticated, Authenticated
}

/** General network configuration. Always include with either [BasicAuthModule] or [TokenAuthModule] */
object NetworkModule {
    val module = module {
        single { BitbucketServiceFactory().produce() }
    }
}

/** Basic auth only configuration. Use this or [TokenAuthModule], never both. **/
private object BasicAuthModule {
    val module = module {
        single { BasicAuthInterceptor(credentialsRepo = get()) }
        single<OkHttpClient>(named(KoinNamedNetwork.Authenticated)) {
            provideBasicAuthOkHttpClient(
                okHttpClient = get(named(KoinNamedNetwork.Unauthenticated)),
                basicAuthInterceptor = get()
            )
        }
    }

    private fun provideBasicAuthOkHttpClient(okHttpClient: OkHttpClient, basicAuthInterceptor: BasicAuthInterceptor): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor(basicAuthInterceptor)
            .build()
    }
}

/** Token auth only configuration. Use this or [BasicAuthModule], never both. **/
object TokenAuthModule {
    val module = module {
        single { TokenAuthInterceptor() }
        single { TokenAuthServiceFactory().produce() }
    }
}
