package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Commit(
    val parents: List<Commit>?,
    val date: String?,
    val message: String?,
    val type: String?,
    val hash: String?
) : Parcelable
