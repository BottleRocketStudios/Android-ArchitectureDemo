package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import kotlinx.coroutines.runBlocking
import org.junit.Test

class BasicAuthRepositoryTest {

    @Test
    fun authInterceptor() {
        System.out.println("Running test")
        val b = BasicAuthRepository()
        runBlocking {
            val p = b.authInterceptor("patentlychris@gmail.com", "password1")
        }
    }
}