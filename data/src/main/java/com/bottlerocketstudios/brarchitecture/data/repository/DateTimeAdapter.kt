package com.bottlerocketstudios.brarchitecture.data.repository

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.time.ZonedDateTime

class DateTimeAdapter {
    @ToJson
    fun toJson(zonedDateTime: ZonedDateTime) = zonedDateTime.toString()

    @FromJson
    fun fromJson(zonedDateTime: String): ZonedDateTime = run {
        try {
            ZonedDateTime.parse(zonedDateTime)
        } catch (exception: Exception) {
            Timber.e(exception, "Failed to parse zonedDateTime")
            ZonedDateTime.now()
        }
    }
}
