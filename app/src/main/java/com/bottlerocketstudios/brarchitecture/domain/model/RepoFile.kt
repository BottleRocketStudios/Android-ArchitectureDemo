package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class RepoFile(
    val type: String?,
    val path: String?,
    val mimetype: String?,
    val attributes: List<String>?,
    val size: Int?,
    val commit: Commit?
) : Parcelable
