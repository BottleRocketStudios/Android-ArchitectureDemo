package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repository(
    val scm: String? = "",
    val name: String? = "",
    val owner: User? = null,
    val is_private: Boolean? = true
) : Parcelable
