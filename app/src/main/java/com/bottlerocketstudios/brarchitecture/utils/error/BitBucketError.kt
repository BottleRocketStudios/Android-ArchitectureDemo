package com.bottlerocketstudios.brarchitecture.utils.error

import kotlinx.serialization.Serializable

@Serializable
data class BitBucketError(val type: String? = null, val error: ErrorDetail? = null) {
    @Serializable
    data class ErrorDetail(
            val message: String? = null,
            val detail: String? = null,
            val id: String? = null
    )

    val message: String?
        get() = error?.message ?: error?.detail
}
