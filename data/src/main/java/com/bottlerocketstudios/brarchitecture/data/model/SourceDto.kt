package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SourceDto(
    @Json(name = "branch") val branch: BranchDto?,
    @Json(name = "commit") val commit: CommitDto?,
    @Json(name = "repository") val prRepository: PrRepositoryDto?,
) : Parcelable, Dto
