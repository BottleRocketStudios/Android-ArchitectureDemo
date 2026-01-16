package com.bottlerocketstudios.brarchitecture.data.di

import android.content.Context
import android.content.SharedPreferences
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogicImpl
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapper
import com.bottlerocketstudios.brarchitecture.data.model.ResponseToApiResultMapperImpl
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketHttpClientFactory
import com.bottlerocketstudios.brarchitecture.data.network.BitbucketServiceKtor
import com.bottlerocketstudios.brarchitecture.data.network.CognitoService
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthServiceKtor
import com.bottlerocketstudios.brarchitecture.data.network.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.repository.CognitoRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.repository.FeatureToggleRepositoryImpl
import com.bottlerocketstudios.brarchitecture.data.serialization.DateTimeSerializer
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.CognitoRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProviderImpl
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.bottlerocketstudios.brarchitecture.data.serialization.ProtectedPropertySerializer
import com.bottlerocketstudios.brarchitecture.data.serialization.ValidCredentialSerializer
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import java.time.Clock
import java.time.ZonedDateTime

/** General app configuration (repositories/viewmodels/etc) */
object DataModule {
    val module = module {
        // Clock for injectable time that can be replaced in tests
        single<Clock> { Clock.systemDefaultZone() }
        single<DispatcherProvider> { DispatcherProviderImpl() }
// ... inside module ...
        single<Json> {
            Json {
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    contextual(ZonedDateTime::class, DateTimeSerializer(clock = get()))
                    contextual(ValidCredentialModel::class, ValidCredentialSerializer)
                    @Suppress("UNCHECKED_CAST")
                    contextual(ProtectedProperty::class, ProtectedPropertySerializer as KSerializer<ProtectedProperty<*>>)
                }
            }
        }
        single<BitbucketRepository> { BitbucketRepositoryImpl() }
        single<CognitoRepository> { CognitoRepositoryImpl(cognitoService = get()) }
        single<FeatureToggleRepository> { FeatureToggleRepositoryImpl(json = get()) }
        single<EnvironmentRepository> { EnvironmentRepositoryImpl(sharedPrefs = get(named(KoinNamedSharedPreferences.Environment)), buildConfigProvider = get()) }
        single<ForceCrashLogic> { ForceCrashLogicImpl(buildConfigProvider = get()) }
        single { BitbucketCredentialsRepository(context = androidContext(), json = get()) }
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

/** General network configuration. Always include with either [BasicAuthModule] or [TokenAuthModule] */
object NetworkModule {
    val module = module {
        single { BitbucketHttpClientFactory() }
        single(named("api")) { get<BitbucketHttpClientFactory>().apiClient }
        single(named("auth")) { get<BitbucketHttpClientFactory>().authClient }
        single { BitbucketServiceKtor() }
        single { CognitoService() }
    }
}

/** Token auth only configuration. Use this or [BasicAuthModule], never both. **/
object TokenAuthModule {
    val module = module {
        single { TokenAuthServiceKtor() }
    }
}
