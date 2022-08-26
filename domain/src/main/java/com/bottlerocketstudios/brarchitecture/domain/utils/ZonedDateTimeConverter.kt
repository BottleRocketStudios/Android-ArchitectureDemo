package com.bottlerocketstudios.brarchitecture.domain.utils

import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Suppress("MagicNumber")
fun ZonedDateTime.convertToTimeAgoMessage(): String {
    val elapsedMinutes = TimeUnit.SECONDS.toMinutes(
        ZonedDateTime.now().toEpochSecond() - this.toEpochSecond()
    )
    val elapsedHours = TimeUnit.MINUTES.toHours(elapsedMinutes)
    val elapsedDays = TimeUnit.MINUTES.toDays(elapsedMinutes)

    return when {
        elapsedMinutes in 5..59 -> "$elapsedMinutes Minutes Ago"
        elapsedHours in 1..23 -> elapsedHours.toInt().timePluralizer("Hour")
        elapsedDays in 1..6 -> elapsedDays.toInt().timePluralizer("Day")
        elapsedDays in 7..30 -> (elapsedDays / 7).toInt().timePluralizer("Week")
        elapsedDays in 31..365 -> (elapsedDays / 30.4).roundToInt().timePluralizer("Month")
        elapsedDays > 364 -> (elapsedDays / 365).toInt().timePluralizer("Year")
        else -> "Just Now"
    }
}

private fun Int.timePluralizer(timeUnit: String) =
    when (this > 1) {
        true -> "$this ${timeUnit}s Ago"
        false -> "$this $timeUnit Ago"
    }
