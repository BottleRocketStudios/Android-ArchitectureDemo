package com.bottlerocketstudios.brarchitecture.infrastructure.network

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient

/** Provides [OkHttpClient.Builder] to allow different production/test implementations */
interface OkHttpBuilderProvider {
    val okHttpClientBuilder: OkHttpClient.Builder
}

/** Returns a [OkHttpClient.Builder] with the [ChuckerInterceptor] attached. */
class DefaultOkHttpBuilderProvider(private val application: Application) : OkHttpBuilderProvider {
    override val okHttpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder().addInterceptor(ChuckerInterceptor(application))
}
