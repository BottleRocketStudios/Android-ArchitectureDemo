package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.converter.convertToGitRepository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.formattedUpdateTime
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
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
        repos.map {
            it.map {
                UserRepositoryUiModel(repo = it.convertToGitRepository()).apply {
                    updatedTimeString = this.repo.updated.formattedUpdateTime(clock).getString()
                }
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
