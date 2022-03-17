package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class RepositoryDto(
    @Json(name = "scm") val scm: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "owner") val owner: UserDto? = null,
    @Json(name = "workspace") val workspaceDto: WorkspaceDto? = null,
    @Json(name = "is_private") val isPrivate: Boolean? = true,
    @Json(name = "description") val description: String? = "",
    @Json(name = "updated_on") val updated: ZonedDateTime? = null
) : Parcelable
