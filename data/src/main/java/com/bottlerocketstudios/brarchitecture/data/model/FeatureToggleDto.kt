package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class FeatureToggleDto(
    @Json(name = "booleanFlags") val booleanFlags: List<FeatureToggleBooleanDto>,
    @Json(name = "stringFlags") val stringFlags: List<FeatureToggleStringDto>
) : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class FeatureToggleBooleanDto(
    @Json(name = "name") val name: String,
    @Json(name = "value") val value: Boolean,
    @Json(name = "defaultValue") val defaultValue: Boolean,
    @Json(name = "requireRestart") val requireRestart: Boolean
)  : Parcelable, Dto

@JsonClass(generateAdapter = true)
@Parcelize
data class FeatureToggleStringDto(
    @Json(name = "name") val name: String,
    @Json(name = "value") val value: String,
    @Json(name = "defaultValue") val defaultValue: String,
    @Json(name = "requireRestart") val requireRestart: Boolean
)  : Parcelable, Dto
