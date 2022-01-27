package com.bottlerocketstudios.brarchitecture.ui.util

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.testContext
import com.google.common.truth.Truth.assertThat

import org.junit.Test
import org.mockito.kotlin.mock

class StringIdHelperTest : BaseTest() {

    @Test
    fun getString_raw_returnsRawString() {
        val sut = StringIdHelper.Raw("foo")

        val result = sut.getString(mock())

        assertThat(result).isEqualTo("foo")
    }

    @Test
    fun getString_id_returnsMatchingIdString() {
        val sut = StringIdHelper.Id(R.string.app_name)

        val result = sut.getString(testContext)

        assertThat(result).isEqualTo("App Name")
    }

    @Test
    fun getString_formatWithStringArgs_returnsFormattedString() {
        val sut = StringIdHelper.Format(R.string.sample_format, listOf("Foo"))

        val result = sut.getString(testContext)

        assertThat(result).isEqualTo("Sample Foo")
    }

    @Test
    fun getString_formatWithStringIdHelperArgs_returnsFormattedString() {
        val sut = StringIdHelper.Format(R.string.sample_format, listOf(StringIdHelper.Id(R.string.app_name)))

        val result = sut.getString(testContext)

        assertThat(result).isEqualTo("Sample App Name")
    }

    @Test
    fun getString_pluralWithStringArgs_returnsFormattedString() {
        val sut = StringIdHelper.Plural(R.plurals.days_ago_plural, 10, listOf(10))

        val result = sut.getString(testContext)

        assertThat(result).isEqualTo("10 days ago")
    }

    @Test
    fun getString_pluralWithStringIdHelperArgs_returnsFormattedString() {
        val sut = StringIdHelper.Plural(R.plurals.sample_plural_format, 10, listOf("Foo".toStringIdHelper(), 10))

        val result = sut.getString(testContext)

        assertThat(result).isEqualTo("Foo was updated 10 days ago")
    }
}
