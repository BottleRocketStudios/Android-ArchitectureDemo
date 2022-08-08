package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class CommitRepositoryDto(
    @Json(name = "name") val name: String?,
    @Json(name = "full_name") val fullName: String?,
    @Json(name = "type") val type: String?
) : Parcelable, Dto
