package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import com.google.common.truth.Truth.assertThat

import org.junit.Test

class ProtectedPropertyTest : BaseTest() {

    @Test
    fun toString_nullValue_returnsStringWithNullValue() {
        val sut = ProtectedProperty(null)

        val result = sut.toString()

        assertThat(result).isEqualTo("ProtectedProperty(value=null)")
    }

    @Test
    fun toString_nonNullValue_returnsStringWithValueRedacted() {
        val sut = ProtectedProperty("foo")

        val result = sut.toString()

        assertThat(result).isEqualTo("ProtectedProperty(value=REDACTED)")
    }

    @Test
    fun getValue_nullValue_returnsNull() {
        val nullString: String? = null
        val sut = ProtectedProperty(nullString)

        val result = sut.value

        assertThat(result).isNull()
    }

    @Test
    fun getValue_nonNullValue_returnsNonNullValue() {
        val sut = ProtectedProperty("foo")

        val result = sut.value

        assertThat(result).isEqualTo("foo")
    }
}
