package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BitbucketFailure (
    val type: String? = "",
    val error: BitbucketError? = null
)

@JsonClass(generateAdapter = true)
data class BitbucketError (
    val message: String? = ""
)