package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TokenAuthRepositoryTest {
    
    @Test
    fun authInterceptor() {
        System.out.println("Running test")
        val okHttp = OkHttpClient.Builder()
            .addInterceptor( Interceptor { 
                System.out.println(it.request().header("Authorization"))
                it.proceed(it.request())
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bitbucket.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            //.client(okHttp)
            .build()
        val b = TokenAuthRepository(retrofit)
        runBlocking {
            val p = b.authInterceptor("patentlychris@gmail.com", "password1")
        }
    }
}