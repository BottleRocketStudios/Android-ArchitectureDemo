package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class SnippetDto(
    @SerialName("id") val id: String? = null,
    @SerialName("workspace") val workspace: WorkspaceDto? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("owner") val owner: UserDto? = null,
    @SerialName("is_private") val isPrivate: Boolean? = null,
    @SerialName("updated_on") @Contextual val updated: ZonedDateTime? = null,
) : Parcelable, Dto
