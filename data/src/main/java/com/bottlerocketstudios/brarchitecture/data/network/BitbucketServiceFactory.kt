package com.bottlerocketstudios.brarchitecture.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BitbucketServiceFactory : ServiceFactory(), KoinComponent {

    private val context: Context by inject()

    override val baseUrl = "https://api.bitbucket.org"
    override val interceptors = listOf(ChuckerInterceptor.Builder(context).build())

    internal fun produce(): BitbucketService = retrofit.create(BitbucketService::class.java)
}
