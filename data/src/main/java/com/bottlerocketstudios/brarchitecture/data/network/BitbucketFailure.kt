package com.bottlerocketstudios.brarchitecture.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Common wrapper for bitbucket error responses.
 * Example api docs showing definitions of below values at https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src#get
 */
@Serializable
internal data class BitbucketFailure(
    /** Base type for most resource objects. It defines the common type element that identifies an object's type. It also identifies the element as Swagger's discriminator. */
    @SerialName("type") val type: String? = "",
    @SerialName("error") val error: BitbucketError? = null
)

@Serializable
internal data class BitbucketError(
    @SerialName("message") val message: String? = ""
)
