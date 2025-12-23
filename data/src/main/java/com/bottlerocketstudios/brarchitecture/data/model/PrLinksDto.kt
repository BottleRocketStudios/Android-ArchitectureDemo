package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PrLinksDto(
    @SerialName("self") val self: LinkDto?,
    @SerialName("html") val html: LinkDto?,
    @SerialName("avatar") val avatar: LinkDto?,
    @SerialName("commits") val commits: LinkDto?,
    @SerialName("approve") val approve: LinkDto?,
    @SerialName("request-changes") val requestChanges: LinkDto?,
    @SerialName("diff") val diff: LinkDto?,
    @SerialName("diffstat") val diffStat: LinkDto?,
    @SerialName("activity") val activity: LinkDto?,
    @SerialName("merge") val merge: LinkDto?,
    @SerialName("decline") val decline: LinkDto?,
    @SerialName("statuses") val statuses: LinkDto?,
) : Parcelable, Dto
