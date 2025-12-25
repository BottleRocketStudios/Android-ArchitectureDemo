package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class SnippetCommentDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("created_on") @Contextual val created: ZonedDateTime? = null,
    @SerialName("updated_on") @Contextual val updated: ZonedDateTime? = null,
    @SerialName("content") val content: SnippetCommentContentDto? = null,
    @SerialName("user") val user: UserDto? = null,
    @SerialName("deleted") val deleted: Boolean? = null,
    @SerialName("parent") val parent: ParentSnippetCommentDto? = null,
    @SerialName("links") val links: LinksDto? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("snippet") val snippet: SnippetDto? = null
) : Parcelable, Dto

@Serializable
@Parcelize
data class SnippetCommentContentDto(
    @SerialName("type") val type: String? = null,
    @SerialName("raw") val raw: String? = null,
    @SerialName("markup") val markup: String? = null,
    @SerialName("html") val html: String? = null
) : Parcelable, Dto

@Serializable
@Parcelize
data class ParentSnippetCommentDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("links") val links: LinksDto? = null,
) : Parcelable, Dto
