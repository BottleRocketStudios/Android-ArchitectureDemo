package com.bottlerocketstudios.brarchitecture.infrastructure.coroutine

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class DispatcherProviderImplTest : BaseTest() {

    @Test
    fun getDefault_matchesCorrespondingDispatchersValue() {
        val sut = DispatcherProviderImpl()
        assertThat(sut.Default).isEqualTo(Dispatchers.Default)
    }

    @Test
    fun getIO_matchesCorrespondingDispatchersValue() {
        val sut = DispatcherProviderImpl()
        assertThat(sut.IO).isEqualTo(Dispatchers.IO)
    }

    @Test
    fun getMain_matchesCorrespondingDispatchersValue() {
        val sut = DispatcherProviderImpl()
        assertThat(sut.Main).isEqualTo(Dispatchers.Main)
    }

    @Test
    fun getUnconfined_matchesCorrespondingDispatchersValue() {
        val sut = DispatcherProviderImpl()
        assertThat(sut.Unconfined).isEqualTo(Dispatchers.Unconfined)
    }
}
