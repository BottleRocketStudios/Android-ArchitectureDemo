package com.bottlerocketstudios.brarchitecture.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepoFile(
    val type: String?,
    val path: String?,
    val mimetype: String?,
    val attributes: List<String>?,
    val size: Int?,
    val commit: Commit?
) : Parcelable
