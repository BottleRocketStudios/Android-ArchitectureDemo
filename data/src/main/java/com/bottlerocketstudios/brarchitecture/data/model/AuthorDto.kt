package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class AuthorDto(
    @Json(name = "user") val userInfo: UserDto?,
    @Json(name = "display_name") val displayName: String?,
    @Json(name = "links") val links: PrLinksDto?,
    @Json(name = "type") val type: String?,
    @Json(name = "uuid") val uuid: String?,
    @Json(name = "account_id") val accountId: String?,
    @Json(name = "nickname") val nickName: String?,
) : Parcelable, Dto
