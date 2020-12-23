package com.bottlerocketstudios.brarchitecture.data.model

import android.content.Context
import android.os.Parcelable
import com.bottlerocketstudios.brarchitecture.data.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@JsonClass(generateAdapter = true)
@Parcelize
data class Repository(
    @Json(name = "scm") val scm: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "owner") val owner: User? = null,
    @Json(name = "is_private") val isPrivate: Boolean? = true,
    @Json(name = "description") val description: String? = "",
    @Json(name = "updated_on") val updated: ZonedDateTime? = null
) : Parcelable {
    fun formattedUpdateTime(context: Context): String {
        val wasUpdated = updated ?: ZonedDateTime.now()
        val daysAgo = wasUpdated.until(ZonedDateTime.now(), ChronoUnit.DAYS)
        if (daysAgo < 7) {
            return context.getString(R.string.days_ago, daysAgo)
        } else {
            return wasUpdated.format(DateTimeFormatter.ISO_DATE)
        }
    }
}
