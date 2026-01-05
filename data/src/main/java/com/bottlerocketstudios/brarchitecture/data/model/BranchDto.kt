package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class BranchDto(
        @SerialName("name") val name: String? = null,
        @SerialName("target") val target: TargetDto? = null
) : Parcelable, Dto
