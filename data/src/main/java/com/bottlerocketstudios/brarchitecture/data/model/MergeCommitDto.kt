package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class MergeCommitDto(
    @Json(name = "type") val type: String?,
    @Json(name = "hash") val hash: String?,
    @Json(name = "links") val links: PrLinksDto?
) : Parcelable
