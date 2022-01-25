package com.bottlerocketstudios.brarchitecture.data.network.auth

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat

import org.junit.Test

class AuthUtilsKtTest : BaseTest() {

    @Test
    fun getBasicAuthHeader_givenInput_returnsExpectedResult() {
        val result = getBasicAuthHeader("foo", "bar")

        assertThat(result).isEqualTo("Basic Zm9vOmJhcg==")
    }
}
