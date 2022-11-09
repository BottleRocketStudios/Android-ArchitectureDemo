package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class ProjectDto(
    @Json(name = "name") val name: String?,
    @Json(name = "key") val key: String?,
    @Json(name = "updated_on") val updated: ZonedDateTime?
) : Parcelable, Dto
