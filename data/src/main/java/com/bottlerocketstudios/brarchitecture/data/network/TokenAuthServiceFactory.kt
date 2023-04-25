package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import org.koin.core.component.inject

class TokenAuthServiceFactory : ServiceFactory() {

    override val baseUrl = "https://bitbucket.org/"
    override val interceptors = listOf(TokenAuthInterceptor())

    internal fun produce(): TokenAuthService = retrofit.create(TokenAuthService::class.java)
}
