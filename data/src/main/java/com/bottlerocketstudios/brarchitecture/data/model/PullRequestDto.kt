package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import java.time.ZonedDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PullRequestDto(
        @SerialName("author") val author: AuthorDto? = null,
        @SerialName("comment_count") val commentCount: Long? = null,
        @SerialName("closed_by") val closedBy: ClosedByDto? = null,
        @SerialName("closed_source_branch") val closedSourceBranch: Boolean? = null,
        @SerialName("created_on") @Contextual val createdOn: ZonedDateTime? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("destination") val destination: DestinationDto? = null,
        @SerialName("id") val id: Long? = null,
        @SerialName("links") val links: PrLinksDto? = null,
        @SerialName("merge_commit") val mergeCommitDto: MergeCommitDto? = null,
        @SerialName("reason") val reason: String? = null,
        @SerialName("source") val source: SourceDto? = null,
        @SerialName("state") val state: String? = null,
        @SerialName("summary") val summary: SummaryDto? = null,
        @SerialName("task_count") val taskCount: Long? = null,
        @SerialName("title") val title: String? = null,
        @SerialName("updated_on") @Contextual val updatedOn: ZonedDateTime? = null,
) : Parcelable, Dto
