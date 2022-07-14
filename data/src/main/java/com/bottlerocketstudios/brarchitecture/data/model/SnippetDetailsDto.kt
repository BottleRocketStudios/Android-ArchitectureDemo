package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetDetailsDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "created_on") val created: ZonedDateTime? = null,
    @Json(name = "updated_on") val updated: ZonedDateTime? = null,
    @Json(name = "is_private") val isPrivate: Boolean? = null,
    @Json(name = "files") val files: List<SnippetDetailsFileDto?>?= null,
    @Json(name = "owner") val owner: UserDto? = null,
    @Json(name = "creator") val creator: UserDto? = null,
    @Json(name = "links") val links: LinksDto? = null
): Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetDetailsFileDto(
    val file: Map<String, LinksDto>? = null
): Parcelable, Dto
