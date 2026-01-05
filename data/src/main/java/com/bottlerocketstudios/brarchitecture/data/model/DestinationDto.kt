package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DestinationDto(
        @SerialName("branch") val branch: BranchDto? = null,
        @SerialName("commit") val commit: CommitDto? = null,
        @SerialName("repository") val prRepository: PrRepositoryDto? = null,
) : Parcelable, Dto
