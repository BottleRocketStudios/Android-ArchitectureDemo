package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ClosedByDto(
    @SerialName("display_name") val displayName: String?,
    @SerialName("links") val linksDto: PrLinksDto?,
    @SerialName("type") val type: String?,
    @SerialName("uuid") val uuid: String?,
    @SerialName("account_id") val accountId: String?,
    @SerialName("nickname") val nickName: String?,
) : Parcelable, Dto
