package com.bottlerocketstudios.brarchitecture.infrastructure.network

data class BitbucketPagedResponse<T>(
    val pagelen: Int = 0,
    val page: Int = 0,
    val size: Int = 0,
    val values: T? = null,
    val next: String? = null,
    val previous: String? = null
)
