package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserDto(
    @SerialName("username") val username: String? = "",
    @SerialName("nickname") val nickname: String? = "",
    @SerialName("account_status") val accountStatus: String? = "",
    @SerialName("display_name") val displayName: String? = "",
    @SerialName("created_on") val createdOn: String? = null,
    @SerialName("uuid") val uuid: String? = "",
    @SerialName("links") val linksDto: LinksDto? = null
) : Parcelable, Dto
