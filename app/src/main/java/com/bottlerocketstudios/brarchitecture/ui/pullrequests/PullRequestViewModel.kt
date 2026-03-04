package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import java.time.Clock
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class PullRequestViewModel : BaseViewModel() {

    // region DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()
    // endregion

    // region UI State
    val selectionList = MutableStateFlow(listOf("Open", "Merged", "Declined", "Superseded"))
    val selectedText = MutableStateFlow("Open")

    val pullRequestList =
            repo.pullRequests.map {
                it.map { dto ->
                    PullRequestItemState(
                            prName = dto.title.asMutableState(),
                            prState = dto.state.asMutableState(),
                            prCreation =
                                    dto.createdOn
                                            ?.formattedUpdateTime(clock)
                                            ?.getString()
                                            .orEmpty()
                                            .asMutableState(),
                            author = dto.author.asMutableState(),
                            source = dto.source.asMutableState(),
                            destination = dto.destination.asMutableState(),
                            // FIXME Pull Request api doesn't return the below values. Get data from
                            // another api call later.
                            linesAdded = "0 Lines Added".asMutableState(),
                            linesRemoved = "0 Lines Removed".asMutableState(),
                            reviewers = "No Reviewers".asMutableState(),
                    )
                }
            }
    // endregion

    // region Init
    init {
        launchIO { selectedText.collect { getPullRequestByState(it) } }
    }
    // endregion

    // region Helpers
    private fun getPullRequestByState(state: String = "Open") {
        launchIO {
            showLoadingIndicator.wrapIndicator {
                repo.getPullRequestsWithQuery(state.uppercase(Locale.ROOT))
                        .onSuccess {}
                        .onFailureLogged(errorStrId = R.string.pull_request_error)
            }
        }
    }
    // endregion
}
