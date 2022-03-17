package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.converter.convertToRepository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(repo: BitbucketRepository) : BaseViewModel() {
    val user = repo.user
    val repos = repo.repos
    val userRepositoryState: Flow<List<UserRepositoryUiModel>> = repos.map { it.map { UserRepositoryUiModel(repo = it.convertToRepository()) } }

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshUser()
            repo.refreshMyRepos()
        }
    }
}
