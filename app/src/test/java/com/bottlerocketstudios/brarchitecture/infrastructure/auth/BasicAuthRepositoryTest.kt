package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import org.junit.Test

class BasicAuthRepositoryTest {

    @Test
    fun authInterceptor() {
        System.out.println("Running test")
        val b = BasicAuthRepository()
        val p = b.authInterceptor("patentlychris@gmail.com", "password1")
    }
}