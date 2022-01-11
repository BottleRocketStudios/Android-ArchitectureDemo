package com.bottlerocketstudios.brarchitecture.ui.util

import android.content.Context
import com.bottlerocketstudios.brarchitecture.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun ZonedDateTime?.formattedUpdateTime(context: Context): String {
    val wasUpdated = this ?: ZonedDateTime.now()
    val daysAgo = wasUpdated.until(ZonedDateTime.now(), ChronoUnit.DAYS).toInt()
    return when {
        daysAgo == 0 -> context.getString(R.string.today)
        daysAgo < ONE_WEEK_IN_DAYS -> context.resources.getQuantityString(R.plurals.days_ago_plural, daysAgo, daysAgo)
        else -> wasUpdated.format(DateTimeFormatter.ISO_DATE)
    }
}

private const val ONE_WEEK_IN_DAYS = 7
