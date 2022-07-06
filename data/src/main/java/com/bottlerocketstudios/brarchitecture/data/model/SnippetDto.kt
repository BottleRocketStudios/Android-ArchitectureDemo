package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class SnippetDto(
    val id: String? = null,
    val workspace: WorkspaceDto? = null,
    val title: String? = null,
    @Json(name = "is_private")
    val isPrivate: Boolean? = null,
    val owner: UserDto? = null,
    @Json(name = "updated_on")
    val updated: ZonedDateTime? = null,
) : Parcelable, Dto
