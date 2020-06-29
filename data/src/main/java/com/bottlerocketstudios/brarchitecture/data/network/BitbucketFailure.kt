package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Common wrapper for bitbucket error responses.
 * Example api docs showing definitions of below values at https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src#get
 */
@JsonClass(generateAdapter = true)
internal data class BitbucketFailure(
    /** Base type for most resource objects. It defines the common type element that identifies an object's type. It also identifies the element as Swagger's discriminator. */
    @Json(name = "type") val type: String? = "",
    @Json(name = "error") val error: BitbucketError? = null
)

@JsonClass(generateAdapter = true)
internal data class BitbucketError(
    @Json(name = "message") val message: String? = ""
)
