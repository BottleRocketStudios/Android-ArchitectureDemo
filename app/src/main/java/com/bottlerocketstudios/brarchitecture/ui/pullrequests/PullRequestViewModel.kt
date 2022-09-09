package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import java.time.Clock
import java.util.Locale

class PullRequestViewModel : BaseViewModel() {

    // DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()

    val selectionList = MutableStateFlow(listOf("Open", "Merged", "Declined", "Superseded"))
    val selectedText = MutableStateFlow("Open")

    val pullRequestList = repo.pullRequests.map {
        it.map { dto ->
            PullRequestItemState(
                prName = dto.title.asMutableState(),
                prState = dto.state.asMutableState(),
                prCreation = dto.createdOn?.formattedUpdateTime(clock)?.getString().orEmpty().asMutableState(),
                // FIXME Pull Request api doesn't return the below values. Get data from another api call later.
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState()
            )
        }
    }

    // Init logic
    init {
        launchIO {
            selectedText.collect {
                getPullRequestByState(it)
            }
        }
    }

    private fun getPullRequestByState(state: String = "Open") {
        launchIO {
            repo.getPullRequestsWithQuery(state.uppercase(Locale.ROOT))
                .handlingErrors(R.string.pull_request_error) {}
        }
    }
}
