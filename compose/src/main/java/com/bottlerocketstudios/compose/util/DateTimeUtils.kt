package com.bottlerocketstudios.compose.util

import com.bottlerocketstudios.compose.R
import java.time.Clock
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun ZonedDateTime?.formattedUpdateTime(clock: Clock): StringIdHelper {
    val wasUpdated = this ?: ZonedDateTime.now(clock)
    val daysAgo = wasUpdated.until(ZonedDateTime.now(clock), ChronoUnit.DAYS).toInt()
    return when {
        daysAgo == 0 -> StringIdHelper.Id(R.string.today)
        daysAgo < ONE_WEEK_IN_DAYS -> StringIdHelper.Plural(R.plurals.days_ago_plural, daysAgo, listOf(daysAgo))
        else -> wasUpdated.format(DateTimeFormatter.ISO_LOCAL_DATE).toStringIdHelper()
    }
}

private const val ONE_WEEK_IN_DAYS = 7
