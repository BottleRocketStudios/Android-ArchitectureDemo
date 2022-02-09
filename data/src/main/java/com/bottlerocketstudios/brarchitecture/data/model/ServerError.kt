package com.bottlerocketstudios.brarchitecture.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/** Represents data from an error response. Can be modified to model data from a normalized error response for custom error UI/logic as needed. */
@JsonClass(generateAdapter = true)
@Parcelize
data class ServerError(
    /** Note that this value is set by the retrofit response rather than being parsed from the backend */
    val httpErrorCode: Int? = null,
    /** Note that this value is set by the retrofit response rather than being parsed from the backend */
    val status: String? = null,
) : Parcelable
