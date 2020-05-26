package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class User(
    val username: String? = "",
    val nickname: String? = "",
    val account_status: String? = "",
    val display_name: String? = "",
    val created_on: String? = null,
    val uuid: String? = ""
) : Parcelable
