package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SummaryDto(
    @SerialName("type") val type: String?,
    @SerialName("raw") val raw: String?,
    @SerialName("markup") val markUp: String?,
    @SerialName("html") val html: String?, // Note: value contains html paragraph tags <p> </p>
) : Parcelable, Dto
