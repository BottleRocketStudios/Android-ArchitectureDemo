package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.time.Clock

class HomeViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()

    // Setup
    val user = repo.user.groundState(null)
    val repos = repo.repos.groundState(emptyList())
    val pullRequests = repo.pullRequests.groundState(emptyList())

    // UI

    val userPullRequestState: Flow<List<PullRequestItemState>> =
        pullRequests.map { pullList ->
            pullList.map { dto ->
                PullRequestItemState(
                    prName = dto.title.asMutableState(),
                    prState = dto.state.asMutableState(),
                    prCreation = dto.createdOn?.formattedUpdateTime(clock)?.getString().orEmpty().asMutableState(),
                    author = dto.author.asMutableState(),
                    source = dto.source.asMutableState(),
                    destination = dto.destination.asMutableState(),
                    // FIXME Pull Request api doesn't return the below values. Get data from another api call later.
                    linesAdded = "0 Lines Added".asMutableState(),
                    linesRemoved = "0 Lines Removed".asMutableState(),
                    reviewers = "No Reviewers".asMutableState()
                )
            }
        }

    val userRepositoryState: Flow<List<UserRepositoryUiModel>> =
        repos.map { repoList ->
            repoList.map {
                UserRepositoryUiModel(
                    repo = it,
                    formattedLastUpdatedTime = it.updated.formattedUpdateTime(clock)
                )
            }
        }

    // Events
    val itemSelected = MutableSharedFlow<UserRepositoryUiModel>()

    // Init logic
    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshUser()
            repo.refreshMyRepos()
            repo.getPullRequests(user.value?.username ?: "")
        }
    }

    // UI Callbacks
    fun selectItem(userRepositoryUiModel: UserRepositoryUiModel) {
        launchIO {
            itemSelected.emit(userRepositoryUiModel)
        }
    }
}
