package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Repository(
    @Json(name = "scm") val scm: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "owner") val owner: User? = null,
    @Json(name = "is_private") val isPrivate: Boolean? = true
) : Parcelable
