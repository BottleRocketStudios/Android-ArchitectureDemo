package com.bottlerocketstudios.brarchitecture.data.network.retrofit

import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class NetworkModuleTest : KoinTest {

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    networkModule
                )
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testAllPartsFullAfterKoinSetup() {
        val underTest: RestaurantRepoInterface by inject()
        assertNotNull("Koin should have set up the repo", underTest)
    }
}
