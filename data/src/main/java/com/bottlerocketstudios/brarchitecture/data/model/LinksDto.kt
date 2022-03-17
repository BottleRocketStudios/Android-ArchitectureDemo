package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class LinksDto(
    @Json(name = "avatar") val avatar: LinkDto? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class LinkDto(
    @Json(name = "href") val href: String? = null
) : Parcelable
