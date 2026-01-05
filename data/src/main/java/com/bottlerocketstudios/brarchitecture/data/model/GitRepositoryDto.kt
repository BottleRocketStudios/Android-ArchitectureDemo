package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import java.time.ZonedDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GitRepositoryDto(
        @SerialName("scm") val scm: String? = "",
        @SerialName("name") val name: String? = "",
        @SerialName("owner") val owner: UserDto? = null,
        @SerialName("workspace") val workspaceDto: WorkspaceDto? = null,
        @SerialName("is_private") val isPrivate: Boolean? = true,
        @SerialName("description") val description: String? = "",
        @SerialName("updated_on") @Contextual val updated: ZonedDateTime? = null,
        @SerialName("slug") val slug: String? = null,
        @SerialName("full_name") val fullName: String? = null,
) : Parcelable, Dto
