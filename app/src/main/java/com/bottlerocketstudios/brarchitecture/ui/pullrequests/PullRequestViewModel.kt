package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
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

    val pullRequestRequestList = repo.pullRequests.map {
        it.map { dto ->
            PullRequestItemState(
                prName = dto.title.orEmpty().asMutableState(),
                prState = dto.state.orEmpty().asMutableState(),
                prCreation = dto.created_on.formattedUpdateTime(clock).getString().asMutableState(),
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
            when (val results = repo.getPullRequestsWithQuery(state.uppercase(Locale.ROOT))) {
                is Status.Success -> {}
                is Status.Failure -> {
                    if (results is Status.Failure.GeneralFailure) {
                        toaster.toast(results.message)
                    }
                }
            }
        }
    }
}
