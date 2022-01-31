package com.bottlerocketstudios.brarchitecture.data.serialization

import com.bottlerocketstudios.brarchitecture.data.model.ProtectedProperty
import com.bottlerocketstudios.brarchitecture.data.model.toProtectedProperty
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat

import org.junit.Test

class ProtectedPropertyAdapterTest : BaseTest() {

    @Test
    fun toJson_nonNullInput_returnsNonNullValue() {
        val sut = ProtectedPropertyAdapter()

        val result = sut.toJson(ProtectedProperty("foo"))

        assertThat(result).isEqualTo("foo")
    }

    @Test
    fun fromJson_nonNullInput_returnsNonNullValue() {
        val sut = ProtectedPropertyAdapter()

        val result = sut.fromJson("foo")

        assertThat(result).isEqualTo("foo".toProtectedProperty())
    }
}
