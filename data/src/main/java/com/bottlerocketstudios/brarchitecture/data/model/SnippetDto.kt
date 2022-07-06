package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "workspace") val workspace: WorkspaceDto? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "owner") val owner: UserDto? = null,
    @Json(name = "is_private") val isPrivate: Boolean? = null,
    @Json(name = "updated_on") val updated: ZonedDateTime? = null,
) : Parcelable, Dto
