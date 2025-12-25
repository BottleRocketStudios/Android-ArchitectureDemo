package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CommitRepositoryDto(
    @SerialName("name") val name: String?,
    @SerialName("full_name") val fullName: String?,
    @SerialName("type") val type: String?
) : Parcelable, Dto
