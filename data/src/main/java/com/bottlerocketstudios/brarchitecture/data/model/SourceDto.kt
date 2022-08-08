package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Source(
    @Json(name = "branch") val branch: Branch?,
    @Json(name = "commit") val commit: Commit?,
    @Json(name = "repository") val prRepository: PrRepositoryDto?,
) : Parcelable
