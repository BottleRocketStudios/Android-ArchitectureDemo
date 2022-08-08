package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.time.Clock

class PullRequestViewModel : BaseViewModel() {

    // DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()

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
        viewModelScope.launch(dispatcherProvider.IO) {
            val results = repo.getPullRequests()
            when (results) {
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
