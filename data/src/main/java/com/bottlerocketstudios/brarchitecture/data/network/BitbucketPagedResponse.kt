package com.bottlerocketstudios.brarchitecture.infrastructure.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Common wrapper for bitbucket api responses.
 * Example api docs showing definitions of below values at https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Bworkspace%7D/%7Brepo_slug%7D/src#get
 */
@JsonClass(generateAdapter = true)
internal data class BitbucketPagedResponse<T>(
    /** Current number of objects on the existing page. The default value is 10 with 100 being the maximum allowed value. Individual APIs may enforce different values. */
    @Json(name = "pagelen") val pageLength: Int = 0,
    /** Page number of the current results */
    @Json(name = "page") val page: Int = 0,
    /** Total number of objects in the response. This is an optional element that is not provided in all responses, as it can be expensive to compute. */
    @Json(name = "size") val size: Int = 0,
    /** Api result */
    @Json(name = "values") val values: T? = null,
    /** Link to the next page if it exists */
    @Json(name = "next") val next: String? = null,
    /**
     * Link to previous page if it exists. A collections first page does not have this value. This is an optional element that is not provided in all responses.
     * Some result sets strictly support forward navigation and never provide previous links. Clients must anticipate that backwards navigation is not always available.
     * Use this link to navigate the result set and refrain from constructing your own URLs.
     */
    @Json(name = "previous") val previous: String? = null
)
