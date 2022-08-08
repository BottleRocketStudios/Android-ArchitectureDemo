package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class AuthorDto(
    @Json(name = "user") val userInfo: UserDto?,
    @Json(name = "type") val type: String?
) : Parcelable, Dto
