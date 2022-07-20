package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetCommentDto(
    @Json(name = "id") val id: Long?,
    @Json(name = "created_on") val created: ZonedDateTime?,
    @Json(name = "updated_on") val updated: ZonedDateTime?,
    @Json(name = "content") val content: SnippetCommentContentDto?,
    @Json(name = "user") val user: UserDto?,
    @Json(name = "deleted") val deleted: Boolean?,
    @Json(name = "links") val links: LinksDto? = null,
    @Json(name = "type") val type: String?,
    @Json(name = "snippet") val snippet: SnippetDto? = null
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetCommentContentDto(
    @Json(name = "type") val type: String?,
    @Json(name = "raw") val raw: String?,
    @Json(name = "markup") val markup: String?,
    @Json(name = "html") val html: String?
) : Parcelable, Dto
