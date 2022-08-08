package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

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
