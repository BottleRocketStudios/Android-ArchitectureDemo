package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.home.UserPullRequestUIModel
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import java.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class HomeViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()
    // endregion

    // region Events
    val repositorySelected: SharedFlow<UserRepositoryUiModel> = event()
    // endregion

    // region UI State
    val user = repo.user.groundState(null)
    val repos = repo.repos.groundState(emptyList())
    val pullRequests = repo.pullRequests.groundState(emptyList())

    val userPullRequestState: Flow<List<UserPullRequestUIModel>> =
            pullRequests.map { pullList ->
                pullList.map {
                    UserPullRequestUIModel(
                            pullRequest = it,
                            formattedLastUpdatedTime = it.createdOn.formattedUpdateTime(clock)
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
    // endregion

    // region Init
    init {
        launchIO {
            showLoadingIndicator.wrapIndicator {
                repo.refreshUser()
                repo.refreshMyRepos()
                repo.getPullRequests()
            }
        }
    }
    // endregion

    // region UI Callbacks
    fun selectRepositoryItem(userRepositoryUiModel: UserRepositoryUiModel) {
        launchIO { repositorySelected.tryEmit(userRepositoryUiModel) }
    }
    // endregion
}
