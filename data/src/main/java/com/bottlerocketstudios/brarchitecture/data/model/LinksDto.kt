package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LinksDto(
    @SerialName("self") val self: LinkDto? = null,
    @SerialName("html") val html: LinkDto? = null,
    @SerialName("comments") val comments: LinkDto? = null,
    @SerialName("watchers") val watchers: LinkDto? = null,
    @SerialName("commits") val commits: LinkDto? = null,
    @SerialName("diff") val diff: LinkDto? = null,
    @SerialName("clone") val clone: List<LinkDto?>? = null,
    @SerialName("patch") val patch: LinkDto? = null,
    @SerialName("avatar") val avatar: LinkDto? = null,
    @SerialName("followers") val followers: LinkDto? = null,
    @SerialName("following") val following: LinkDto? = null,
    @SerialName("repositories") val repositories: LinkDto? = null,
) : Parcelable, Dto

@Serializable
@Parcelize
data class LinkDto(
    @SerialName("href") val href: String? = null,
    @SerialName("name") val name: String? = null,
) : Parcelable, Dto
