package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Commit(
    @Json(name = "parents") val parents: List<Commit>?,
    @Json(name = "date") val date: String?,
    @Json(name = "message") val message: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "hash") val hash: String?
) : Parcelable
