package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class SnippetDetailsDto(
    @SerialName("id") val id: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("created_on") @Contextual val created: ZonedDateTime? = null,
    @SerialName("updated_on") @Contextual val updated: ZonedDateTime? = null,
    @SerialName("is_private") val isPrivate: Boolean? = null,
    @SerialName("files") val files: Map<String, LinksDto>? = null,
    @SerialName("owner") val owner: UserDto? = null,
    @SerialName("creator") val creator: UserDto? = null,
    @SerialName("links") val links: LinksDto? = null
) : Parcelable, Dto
