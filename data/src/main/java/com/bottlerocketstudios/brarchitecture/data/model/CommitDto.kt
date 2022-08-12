package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class CommitDto(
    @Json(name = "parents") val parents: List<CommitDto>?,
    @Json(name = "date") val date: ZonedDateTime?,
    @Json(name = "message") val message: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "hash") val hash: String?,
    @Json(name = "author") val author: AuthorDto?,
    @Json(name = "repository") val commitRepository: CommitRepositoryDto?,
) : Parcelable, Dto
