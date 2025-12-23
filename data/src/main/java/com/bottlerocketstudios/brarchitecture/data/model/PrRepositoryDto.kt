package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PrRepositoryDto(
    @SerialName("type") val type: String?,
    @SerialName("full_name") val fullName: String?,
    @SerialName("links") val links: LinksDto?,
    @SerialName("uuid") val uuid: String?,
) : Parcelable, Dto
