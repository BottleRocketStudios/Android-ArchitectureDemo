package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FeatureToggleDto(
    @SerialName("booleanFlags") val booleanFlags: List<FeatureToggleBooleanDto>,
    @SerialName("stringFlags") val stringFlags: List<FeatureToggleStringDto>
) : Parcelable, Dto

@Serializable
@Parcelize
data class FeatureToggleBooleanDto(
    @SerialName("name") val name: String,
    @SerialName("value") val value: Boolean,
    @SerialName("defaultValue") val defaultValue: Boolean,
    @SerialName("requireRestart") val requireRestart: Boolean
) : Parcelable, Dto

@Serializable
@Parcelize
data class FeatureToggleStringDto(
    @SerialName("name") val name: String,
    @SerialName("value") val value: String,
    @SerialName("defaultValue") val defaultValue: String,
    @SerialName("requireRestart") val requireRestart: Boolean
) : Parcelable, Dto
