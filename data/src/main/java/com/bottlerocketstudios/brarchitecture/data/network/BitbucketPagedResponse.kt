package com.bottlerocketstudios.brarchitecture.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Common wrapper for bitbucket api responses.
 * Example api docs showing definitions of below values at https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src#get
 */
@Serializable
internal data class BitbucketPagedResponse<T>(
    /** Current number of objects on the existing page. The default value is 10 with 100 being the maximum allowed value. Individual APIs may enforce different values. */
    @SerialName("pagelen") val pageLength: Int = 0,
    /** Page number of the current results */
    @SerialName("page") val page: Int = 0,
    /** Total number of objects in the response. This is an optional element that is not provided in all responses, as it can be expensive to compute. */
    @SerialName("size") val size: Int = 0,
    /** Api result */
    @SerialName("values") val values: T? = null,
    /** Link to the next page if it exists */
    @SerialName("next") val next: String? = null,
    /**
     * Link to previous page if it exists. A collections first page does not have this value. This is an optional element that is not provided in all responses.
     * Some result sets strictly support forward navigation and never provide previous links. Clients must anticipate that backwards navigation is not always available.
     * Use this link to navigate the result set and refrain from constructing your own URLs.
     */
    @SerialName("previous") val previous: String? = null
)
