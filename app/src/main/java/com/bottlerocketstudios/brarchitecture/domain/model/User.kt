package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val username: String? = "",
    val nickname: String? = "",
    val account_status: String? = "",
    val display_name: String? = "",
    val created_on: String? = null,
    val uuid: String? = ""
) : Parcelable
