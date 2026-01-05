package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AuthorDto(
        @SerialName("user") val userInfo: UserDto? = null,
        @SerialName("display_name") val displayName: String? = null,
        @SerialName("links") val links: PrLinksDto? = null,
        @SerialName("type") val type: String? = null,
        @SerialName("uuid") val uuid: String? = null,
        @SerialName("account_id") val accountId: String? = null,
        @SerialName("nickname") val nickName: String? = null,
) : Parcelable, Dto
