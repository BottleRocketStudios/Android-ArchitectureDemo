package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class ProjectDto(
    @SerialName("name") val name: String?,
    @SerialName("key") val key: String?,
    @SerialName("updated_on") @Contextual val updated: ZonedDateTime?
) : Parcelable, Dto
