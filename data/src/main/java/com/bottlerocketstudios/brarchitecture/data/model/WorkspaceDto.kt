package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class WorkspaceDto(
    @Json(name = "slug") val slug: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "uuid") val uuid: String? = ""
) : Parcelable
