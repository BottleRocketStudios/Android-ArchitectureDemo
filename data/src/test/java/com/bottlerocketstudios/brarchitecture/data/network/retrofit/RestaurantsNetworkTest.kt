package com.bottlerocketstudios.brarchitecture.data.network.retrofit

import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
class RestaurantsNetworkTest : KoinTest, BaseTest() {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        val testDataModule = module {
            single<DispatcherProvider>(override = true) {
                TestDispatcherProvider(coroutinesTestRule.getDispatcher())
            }
            single<RestaurantRepoInterface>(override = true) { RestaurantRepoImpl(get(), get()) }
        }

        startKoin {
            modules(
                listOf(
                    networkModule,
                    testDataModule
                )
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun canGetListOfRestaurants() = coroutinesTestRule.getDispatcher().runBlockingTest {
        val underTest: RestaurantRepoInterface by inject()
        val underTestImpl = underTest as RestaurantRepoImpl
        val dispatcher = underTestImpl.dispatcher
        assertTrue(dispatcher is TestDispatcherProvider)
        val result = underTest.getAllRestaurants()
        assertTrue(result is ApiResult.Loading)
    }
}
