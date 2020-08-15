package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Links (
    @Json(name = "avatar") val avatar: Link? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Link (
    @Json(name = "href") val href: String? = null
) : Parcelable


