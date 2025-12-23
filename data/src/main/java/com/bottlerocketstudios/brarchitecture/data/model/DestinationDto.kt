package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DestinationDto(
    @SerialName("type") val type: String?,
    @SerialName("raw") val raw: String?,
    @SerialName("markup") val markUp: String?,
    @SerialName("html") val createdOn: String?,
    @SerialName("branch") val branch: BranchDto?,
) : Parcelable, Dto
