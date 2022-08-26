package com.bottlerocketstudios.brarchitecture.domain.utils

import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

fun ZonedDateTime.convertToTimeAgoMessage(): String {
    val elapsedMinutes = TimeUnit.SECONDS.toMinutes(
        ZonedDateTime.now().toEpochSecond() - this.toEpochSecond()
    )
    val elapsedHours = TimeUnit.MINUTES.toHours(elapsedMinutes)
    val elapsedDays = TimeUnit.MINUTES.toDays(elapsedMinutes)

    return when {
        elapsedMinutes in FIVE_MINUTES until ONE_HOUR_IN_MINUTES -> "$elapsedMinutes Minutes Ago"
        elapsedHours in ONE_HOUR until ONE_DAY_IN_HOURS -> elapsedHours.toInt().timePluralizer("Hour")
        elapsedDays in ONE_DAY until ONE_WEEK_IN_DAYS -> elapsedDays.toInt().timePluralizer("Day")
        elapsedDays in ONE_WEEK_IN_DAYS until ONE_TYPICAL_MONTH_IN_DAYS -> (elapsedDays / ONE_WEEK_IN_DAYS).toInt().timePluralizer("Week")
        elapsedDays in ONE_TYPICAL_MONTH_IN_DAYS until ONE_TYPICAL_YEAR_IN_DAYS -> (elapsedDays / PRECISE_DAYS_IN_ONE_MONTH).roundToInt().timePluralizer("Month")
        elapsedDays >= ONE_TYPICAL_YEAR_IN_DAYS -> (elapsedDays / ONE_TYPICAL_YEAR_IN_DAYS).toInt().timePluralizer("Year")
        else -> "Just Now"
    }
}

private const val FIVE_MINUTES = 5
private const val ONE_HOUR = 1
private const val ONE_DAY = 1
private const val ONE_HOUR_IN_MINUTES = 60
private const val ONE_DAY_IN_HOURS = 24
private const val ONE_WEEK_IN_DAYS = 7
private const val ONE_TYPICAL_MONTH_IN_DAYS = 31
private const val ONE_TYPICAL_YEAR_IN_DAYS = 365
private const val MONTHS_IN_A_YEAR = 12
private const val PRECISE_DAYS_IN_ONE_MONTH: Double = ONE_TYPICAL_YEAR_IN_DAYS.toDouble() / MONTHS_IN_A_YEAR.toDouble()

private fun Int.timePluralizer(timeUnit: String) =
    when (this > 1) {
        true -> "$this ${timeUnit}s Ago"
        false -> "$this $timeUnit Ago"
    }
