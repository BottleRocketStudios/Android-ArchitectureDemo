package com.bottlerocketstudios.compose.home

import com.bottlerocketstudios.brarchitecture.domain.models.PullRequest
import com.bottlerocketstudios.compose.util.StringIdHelper

data class UserPullRequestUIModel(
    val pullRequest: PullRequest,
    val formattedLastUpdatedTime: StringIdHelper,
)
