package com.bottlerocketstudios.compose.util

import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeUtilsKtTest : BaseTest() {

    @Test
    fun formattedUpdateTime_nullInput_returnsTodayOutput() {
        val fixedClock = Clock.fixed(
            Instant.parse("2022-01-26T13:00:00.00Z"),
            ZoneId.of("UTC"),
        )
        val sut: ZonedDateTime? = null

        val result = sut.formattedUpdateTime(fixedClock)

        assertThat(result).isEqualTo(StringIdHelper.Id(R.string.today))
    }

    @Test
    fun formattedUpdateTime_sameDay_returnsTodayOutput() {
        val fixedClock = Clock.fixed(
            Instant.parse("2022-01-26T13:00:00.00Z"),
            ZoneId.of("UTC"),
        )
        val sut = ZonedDateTime.now(
            Clock.fixed(
                Instant.parse("2022-01-26T08:00:00.00Z"),
                ZoneId.of("UTC"),
            )
        )

        val result = sut.formattedUpdateTime(fixedClock)

        assertThat(result).isEqualTo(StringIdHelper.Id(R.string.today))
    }

    @Test
    fun formattedUpdateTime_oneDayAgo_returnsAppropriatePluralString() {
        val fixedClock = Clock.fixed(
            Instant.parse("2022-01-26T13:00:00.00Z"),
            ZoneId.of("UTC"),
        )
        val sut = ZonedDateTime.now(
            Clock.fixed(
                Instant.parse("2022-01-25T08:00:00.00Z"),
                ZoneId.of("UTC"),
            )
        )

        val result = sut.formattedUpdateTime(fixedClock)

        assertThat(result).isEqualTo(StringIdHelper.Plural(R.plurals.days_ago_plural, 1, listOf(1)))
    }

    @Test
    fun formattedUpdateTime_sixDaysAgo_returnsAppropriatePluralString() {
        val fixedClock = Clock.fixed(
            Instant.parse("2022-01-26T13:00:00.00Z"),
            ZoneId.of("UTC"),
        )
        val sut = ZonedDateTime.now(
            Clock.fixed(
                Instant.parse("2022-01-20T08:00:00.00Z"),
                ZoneId.of("UTC"),
            )
        )

        val result = sut.formattedUpdateTime(fixedClock)

        assertThat(result).isEqualTo(StringIdHelper.Plural(R.plurals.days_ago_plural, 6, listOf(6)))
    }

    @Test
    fun formattedUpdateTime_sevenDaysAgo_returnsFormattedDateTime() {
        val fixedClock = Clock.fixed(
            Instant.parse("2022-01-26T05:00:00.00Z"),
            ZoneId.of("UTC"),
        )
        val sut = ZonedDateTime.now(
            Clock.fixed(
                Instant.parse("2022-01-19T05:00:00.00Z"),
                ZoneId.of("UTC"),
            )
        )

        val result = sut.formattedUpdateTime(fixedClock)

        assertThat(result).isEqualTo("2022-01-19".toStringIdHelper())
    }

    @Test
    fun formattedUpdateTime_sevenDaysAgoAltTimezone_returnsFormattedDateTimeForTimeZone() {
        val fixedClock = Clock.fixed(
            Instant.parse("2022-01-26T05:00:00.00Z"),
            ZoneId.of("UTC"),
        )
        val sut = ZonedDateTime.now(
            Clock.fixed(
                Instant.parse("2022-01-19T05:00:00.00Z"),
                ZoneId.of("America/Chicago"),
            )
        )

        val result = sut.formattedUpdateTime(fixedClock)

        assertThat(result).isEqualTo("2022-01-18".toStringIdHelper())
    }
}
