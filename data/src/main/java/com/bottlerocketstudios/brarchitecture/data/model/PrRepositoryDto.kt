package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PrRepositoryDto(
    @Json(name = "type") val type: String?,
    @Json(name = "full_name") val fullName: String?,
    @Json(name = "links") val links: LinksDto?,
    @Json(name = "uuid") val uuid: String?,
) : Parcelable
