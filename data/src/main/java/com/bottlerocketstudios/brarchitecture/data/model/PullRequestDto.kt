package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class PullRequestDto(
    @Json(name = "author") val author: AuthorDto?,
    @Json(name = "comment_count") val comment_count: Long?,
    @Json(name = "closed_by") val closed_by: String?,
    @Json(name = "created_on") val created_on: ZonedDateTime?,
    @Json(name = "description") val description: String?,
    @Json(name = "destination") val destination: DestinationDto?,
    @Json(name = "id") val id: Long?,
    @Json(name = "links") val links: PrLinksDto?,
    @Json(name = "merge_commit") val merge_commit: String?,
    @Json(name = "reason") val reason: String?,
    @Json(name = "source") val source: SourceDto?,
    @Json(name = "state") val state: String?,
    @Json(name = "summary") val summary: SummaryDto?,
    @Json(name = "task_count") val task_count: Long?,
    @Json(name = "title") val title: String?,
    @Json(name = "updated_on") val updated_on: ZonedDateTime?,
) : Parcelable, Dto
