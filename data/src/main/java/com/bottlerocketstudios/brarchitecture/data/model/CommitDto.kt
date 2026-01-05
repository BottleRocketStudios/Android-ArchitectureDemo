package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import java.time.ZonedDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CommitDto(
        @SerialName("parents") val parents: List<CommitDto>? = null,
        @SerialName("date") @Contextual val date: ZonedDateTime? = null,
        @SerialName("message") val message: String? = null,
        @SerialName("type") val type: String? = null,
        @SerialName("hash") val hash: String? = null,
        @SerialName("author") val author: AuthorDto? = null,
        @SerialName("repository") val commitRepository: CommitRepositoryDto? = null,
        @SerialName("links") val links: LinksDto? = null,
) : Parcelable, Dto
