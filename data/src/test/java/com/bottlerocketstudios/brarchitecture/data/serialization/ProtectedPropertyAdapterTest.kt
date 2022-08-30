package com.bottlerocketstudios.brarchitecture.data.serialization

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import com.bottlerocketstudios.brarchitecture.domain.utils.toProtectedProperty
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
