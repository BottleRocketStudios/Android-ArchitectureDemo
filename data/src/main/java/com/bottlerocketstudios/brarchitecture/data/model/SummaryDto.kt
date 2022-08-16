package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SummaryDto(
    @Json(name = "type") val type: String?,
    @Json(name = "raw") val raw: String?,
    @Json(name = "markup") val markUp: String?,
    @Json(name = "html") val html: String?, // Note: value contains html paragraph tags <p> </p>
) : Parcelable, Dto
