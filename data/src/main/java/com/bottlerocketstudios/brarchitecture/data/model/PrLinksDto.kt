package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PrLinksDto(
        @SerialName("self") val self: LinkDto? = null,
        @SerialName("html") val html: LinkDto? = null,
        @SerialName("avatar") val avatar: LinkDto? = null,
        @SerialName("commits") val commits: LinkDto? = null,
        @SerialName("approve") val approve: LinkDto? = null,
        @SerialName("request-changes") val requestChanges: LinkDto? = null,
        @SerialName("diff") val diff: LinkDto? = null,
        @SerialName("diffstat") val diffStat: LinkDto? = null,
        @SerialName("activity") val activity: LinkDto? = null,
        @SerialName("merge") val merge: LinkDto? = null,
        @SerialName("decline") val decline: LinkDto? = null,
        @SerialName("statuses") val statuses: LinkDto? = null,
) : Parcelable, Dto
