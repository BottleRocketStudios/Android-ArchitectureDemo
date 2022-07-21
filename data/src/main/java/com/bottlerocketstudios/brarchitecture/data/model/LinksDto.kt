package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class LinksDto(
    @Json(name = "self") val self: LinkDto?,
    @Json(name = "html") val html: LinkDto?,
    @Json(name = "comments") val comments: LinkDto?,
    @Json(name = "watchers") val watchers: LinkDto?,
    @Json(name = "commits") val commits: LinkDto?,
    @Json(name = "diff") val diff: LinkDto?,
    @Json(name = "clone") val clone: List<LinkDto?>?,
    @Json(name = "patch") val patch: LinkDto?,
    @Json(name = "avatar") val avatar: LinkDto?,
    @Json(name = "followers") val followers: LinkDto?,
    @Json(name = "following") val following: LinkDto?,
    @Json(name = "repositories") val repositories: LinkDto?
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class LinkDto(
    @Json(name = "href") val href: String?,
    @Json(name = "name") val name: String?
) : Parcelable, Dto
