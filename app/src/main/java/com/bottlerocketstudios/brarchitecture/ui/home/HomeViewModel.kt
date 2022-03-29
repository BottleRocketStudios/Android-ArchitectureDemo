package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.converter.convertToGitRepository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.time.Clock

class HomeViewModel(repo: BitbucketRepository) : BaseViewModel() {
    private val clock by inject<Clock>()
    val user = repo.user
    val repos = repo.repos
    val userRepositoryState: Flow<List<UserRepositoryUiModel>> = repos.map {
        it.map {
            UserRepositoryUiModel(
                repo = it.convertToGitRepository(),
                formattedLastUpdatedTime = it.updated.formattedUpdateTime(clock = clock)
            )
        }
    }

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshUser()
            repo.refreshMyRepos()
        }
    }
}
