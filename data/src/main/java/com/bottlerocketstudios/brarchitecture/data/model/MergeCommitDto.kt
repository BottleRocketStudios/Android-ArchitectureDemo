package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MergeCommitDto(
    @SerialName("type") val type: String?,
    @SerialName("hash") val hash: String?,
    @SerialName("links") val links: PrLinksDto?
) : Parcelable, Dto
