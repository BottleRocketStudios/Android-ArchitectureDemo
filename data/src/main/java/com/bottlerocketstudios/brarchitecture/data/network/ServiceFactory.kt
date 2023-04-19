package com.bottlerocketstudios.brarchitecture.data.network

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

abstract class ServiceFactory : KoinComponent {

    private val moshi: Moshi by inject()

    abstract val baseUrl: String
    internal open val interceptors: List<Interceptor> = emptyList()

    protected val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            interceptors().addAll(interceptors)
        }.build()
    }
}
