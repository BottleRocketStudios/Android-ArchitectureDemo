package com.bottlerocketstudios.brarchitecture

import com.bottlerocketstudios.brarchitecture.infrastructure.network.OkHttpBuilderProvider
import okhttp3.OkHttpClient

object TestOkHttpBuilderProvider : OkHttpBuilderProvider {
    override val okHttpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder()
}
