package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class RepoFile(
    @Json(name = "type") val type: String?,
    @Json(name = "path") val path: String?,
    @Json(name = "mimetype") val mimetype: String?,
    @Json(name = "attributes") val attributes: List<String>?,
    @Json(name = "size") val size: Int?,
    @Json(name = "commit") val commit: Commit?
) : Parcelable
