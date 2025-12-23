package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RepoFileDto(
    @SerialName("type") val type: String?,
    @SerialName("path") val path: String?,
    @SerialName("mimetype") val mimetype: String?,
    @SerialName("attributes") val attributes: List<String>?,
    @SerialName("size") val size: Int?,
    @SerialName("commit") val commit: CommitDto?
) : Parcelable, Dto
