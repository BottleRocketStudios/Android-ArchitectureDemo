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
    @Json(name = "comment_count") val commentCount: Long?,
    @Json(name = "closed_by") val closedBy: ClosedByDto?,
    @Json(name = "closed_source_branch") val closedSourceBranch: Boolean?,
    @Json(name = "created_on") val createdOn: ZonedDateTime?,
    @Json(name = "description") val description: String?,
    @Json(name = "destination") val destination: DestinationDto?,
    @Json(name = "id") val id: Long?,
    @Json(name = "links") val links: PrLinksDto?,
    @Json(name = "merge_commit") val mergeCommitDto: MergeCommitDto?,
    @Json(name = "reason") val reason: String?,
    @Json(name = "source") val source: SourceDto?,
    @Json(name = "state") val state: String?,
    @Json(name = "summary") val summary: SummaryDto?,
    @Json(name = "task_count") val taskCount: Long?,
    @Json(name = "title") val title: String?,
    @Json(name = "updated_on") val updatedOn: ZonedDateTime?,
) : Parcelable, Dto
