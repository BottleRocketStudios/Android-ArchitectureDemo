package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetCommentDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "created_on") val created: ZonedDateTime? = null,
    @Json(name = "updated_on") val updated: ZonedDateTime? = null,
    @Json(name = "content") val content: SnippetCommentContentDto? = null,
    @Json(name = "user") val user: UserDto? = null,
    @Json(name = "deleted") val deleted: Boolean? = null,
    @Json(name = "parent") val parent: ParentSnippetCommentDto? = null,
    @Json(name = "links") val links: LinksDto? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "snippet") val snippet: SnippetDto? = null
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetCommentContentDto(
    @Json(name = "type") val type: String? = null,
    @Json(name = "raw") val raw: String? = null,
    @Json(name = "markup") val markup: String? = null,
    @Json(name = "html") val html: String? = null
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class ParentSnippetCommentDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "links") val links: LinksDto? = null,
) : Parcelable, Dto
