package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class WorkspaceDto(
    @SerialName("slug") val slug: String? = "",
    @SerialName("name") val name: String? = "",
    @SerialName("uuid") val uuid: String? = ""
) : Parcelable, Dto
