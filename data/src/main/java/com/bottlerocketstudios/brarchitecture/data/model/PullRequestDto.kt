package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class PullRequestDto(
    @SerialName("author") val author: AuthorDto?,
    @SerialName("comment_count") val commentCount: Long?,
    @SerialName("closed_by") val closedBy: ClosedByDto?,
    @SerialName("closed_source_branch") val closedSourceBranch: Boolean?,
    @SerialName("created_on") @Contextual val createdOn: ZonedDateTime?,
    @SerialName("description") val description: String?,
    @SerialName("destination") val destination: DestinationDto?,
    @SerialName("id") val id: Long?,
    @SerialName("links") val links: PrLinksDto?,
    @SerialName("merge_commit") val mergeCommitDto: MergeCommitDto?,
    @SerialName("reason") val reason: String?,
    @SerialName("source") val source: SourceDto?,
    @SerialName("state") val state: String?,
    @SerialName("summary") val summary: SummaryDto?,
    @SerialName("task_count") val taskCount: Long?,
    @SerialName("title") val title: String?,
    @SerialName("updated_on") @Contextual val updatedOn: ZonedDateTime?,
) : Parcelable, Dto
