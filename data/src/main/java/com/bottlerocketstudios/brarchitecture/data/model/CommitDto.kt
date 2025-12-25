package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class CommitDto(
    @SerialName("parents") val parents: List<CommitDto>?,
    @SerialName("date") @Contextual val date: ZonedDateTime?,
    @SerialName("message") val message: String?,
    @SerialName("type") val type: String?,
    @SerialName("hash") val hash: String?,
    @SerialName("author") val author: AuthorDto?,
    @SerialName("repository") val commitRepository: CommitRepositoryDto?,
    @SerialName("links") val links: LinksDto?,
) : Parcelable, Dto
