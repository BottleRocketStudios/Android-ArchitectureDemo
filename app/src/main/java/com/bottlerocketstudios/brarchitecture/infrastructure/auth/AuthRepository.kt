package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import okhttp3.Interceptor


interface AuthRepository {
    fun authInterceptor(username: String, password: String): Interceptor
}