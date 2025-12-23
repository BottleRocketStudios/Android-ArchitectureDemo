package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class GitRepositoryDto(
    @SerialName("scm") val scm: String? = "",
    @SerialName("name") val name: String? = "",
    @SerialName("owner") val owner: UserDto? = null,
    @SerialName("workspace") val workspaceDto: WorkspaceDto? = null,
    @SerialName("is_private") val isPrivate: Boolean? = true,
    @SerialName("description") val description: String? = "",
    @SerialName("updated_on") @Contextual val updated: ZonedDateTime? = null
) : Parcelable, Dto
