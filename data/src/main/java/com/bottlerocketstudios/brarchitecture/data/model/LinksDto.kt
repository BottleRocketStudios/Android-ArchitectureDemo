package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class LinksDto(
    @Json(name = "self") val self: LinkDto? = null,
    @Json(name = "html") val html: LinkDto? = null,
    @Json(name = "comments") val comments: LinkDto? = null,
    @Json(name = "watchers") val watchers: LinkDto? = null,
    @Json(name = "commits") val commits: LinkDto? = null,
    @Json(name = "diff") val diff: LinkDto? = null,
    @Json(name = "clone") val clone: List<LinkDto?>? = null,
    @Json(name = "patch") val patch: LinkDto? = null,
    @Json(name = "avatar") val avatar: LinkDto? = null,
    @Json(name = "followers") val followers: LinkDto? = null,
    @Json(name = "following") val following: LinkDto? = null,
    @Json(name = "repositories") val repositories: LinkDto? = null
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class LinkDto(
    @Json(name = "href") val href: String? = null,
    @Json(name = "name") val name: String? = null
) : Parcelable, Dto
