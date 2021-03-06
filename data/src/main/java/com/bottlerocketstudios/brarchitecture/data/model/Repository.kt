package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class Repository(
    @Json(name = "scm") val scm: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "owner") val owner: User? = null,
    @Json(name = "workspace") val workspace: Workspace? = null,
    @Json(name = "is_private") val isPrivate: Boolean? = true,
    @Json(name = "description") val description: String? = "",
    @Json(name = "updated_on") val updated: ZonedDateTime? = null
) : Parcelable
