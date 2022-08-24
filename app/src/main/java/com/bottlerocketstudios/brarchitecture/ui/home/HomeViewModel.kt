package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
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
    val user = repo.user
    val repos = repo.repos

    // UI
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
        }
    }

    // UI Callbacks
    fun selectItem(userRepositoryUiModel: UserRepositoryUiModel) {
        launchIO {
            itemSelected.emit(userRepositoryUiModel)
        }
    }
}
