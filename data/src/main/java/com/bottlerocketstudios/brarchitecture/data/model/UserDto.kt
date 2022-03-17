package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserDto(
    @Json(name = "username") val username: String? = "",
    @Json(name = "nickname") val nickname: String? = "",
    @Json(name = "account_status") val accountStatus: String? = "",
    @Json(name = "display_name") val displayName: String? = "",
    @Json(name = "created_on") val createdOn: String? = null,
    @Json(name = "uuid") val uuid: String? = "",
    @Json(name = "links") val linksDto: LinksDto? = null
) : Parcelable
