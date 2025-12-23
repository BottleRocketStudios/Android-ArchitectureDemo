package com.bottlerocketstudios.brarchitecture.data.network

import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthInterceptor
import com.bottlerocketstudios.brarchitecture.data.network.auth.token.TokenAuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TokenAuthServiceFactory : ServiceFactory(), KoinComponent {

    override val baseUrl = "https://bitbucket.org/"
    private val tokenAuthInterceptor: TokenAuthInterceptor by inject()
    override val interceptors = listOf(tokenAuthInterceptor)

    internal fun produce(): TokenAuthService = retrofit.create(TokenAuthService::class.java)
}
