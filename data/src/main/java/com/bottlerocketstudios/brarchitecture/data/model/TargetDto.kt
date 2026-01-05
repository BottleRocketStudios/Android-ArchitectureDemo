package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import java.time.ZonedDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TargetDto(@SerialName("date") @Contextual val date: ZonedDateTime? = null) :
        Parcelable, Dto
