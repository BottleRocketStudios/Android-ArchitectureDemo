package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
@Parcelize
data class PullRequestDto(
    @Json(name = "author") val author: Author?,
    @Json(name = "comment_count") val comment_count: Long?,
    @Json(name = "closed_by") val closed_by: String?,
    @Json(name = "created_on") val created_on: ZonedDateTime?,
    @Json(name = "description") val description: String?,
    @Json(name = "destination") val destination: Destination?,
    @Json(name = "id") val id: Long?,
    @Json(name = "links") val links: PrLinksDto?,
    @Json(name = "merge_commit") val merge_commit: String?,
    @Json(name = "reason") val reason: String?,
    @Json(name = "source") val source: Source?,
    @Json(name = "state") val state: String?,
    @Json(name = "summary") val summary: Summary?,
    @Json(name = "task_count") val task_count: Long?,
    @Json(name = "title") val title: String?,
    @Json(name = "updated_on") val updated_on: ZonedDateTime?,
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class Destination(
    @Json(name = "type") val type: String?,
    @Json(name = "raw") val raw: String?,
    @Json(name = "markup") val markUp: String?,
    @Json(name = "html") val created_on: String?,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class PrLinksDto(
    @Json(name = "self") val self: LinkDto?,
    @Json(name = "html") val html: LinkDto?,
    @Json(name = "avatar") val avatar: LinkDto?,
    @Json(name = "commits") val commits: LinkDto?,
    @Json(name = "approve") val approve: LinkDto?,
    @Json(name = "request-changes") val requestChanges: LinkDto?,
    @Json(name = "diff") val diff: LinkDto?,
    @Json(name = "diffstat") val diffStat: LinkDto?,
    @Json(name = "activity") val activity: LinkDto?,
    @Json(name = "merge") val merge: LinkDto?,
    @Json(name = "decline") val decline: LinkDto?,
    @Json(name = "statuses") val statuses: LinkDto?,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Author(
    @Json(name = "display_name") val displayName: String?,
    @Json(name = "links") val links: PrLinksDto?,
    @Json(name = "type") val type: String?,
    @Json(name = "uuid") val uuid: String?,
    @Json(name = "account_id") val accountId: String?,
    @Json(name = "nickname") val nickName: String?,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Summary(
    @Json(name = "type") val type: String?,
    @Json(name = "raw") val raw: String?,
    @Json(name = "markup") val markUp: String?,
    @Json(name = "html") val created_on: String?,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Source(
    @Json(name = "branch") val branch: Branch?,
    @Json(name = "commit") val commit: Commit?,
    @Json(name = "repository") val prRepository: PrRepositoryDto?,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Branch(@Json(name = "name") val name: String?) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class PrRepositoryDto(
    @Json(name = "type") val type: String?,
    @Json(name = "full_name") val fullName: String?,
    @Json(name = "links") val links: LinksDto?,
    @Json(name = "uuid") val uuid: String?,
) : Parcelable
