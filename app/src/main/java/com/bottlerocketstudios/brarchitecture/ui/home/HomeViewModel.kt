package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.HeaderViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.StringIdHelper
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel() {
    val user = repo.user
    val repos = repo.repos
    val reposGroup = Section()

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repos.collect { repoList ->
                val map = repoList.map { RepositoryViewModel(it) }
                withContext(dispatcherProvider.Main) {
                    reposGroup.setHeader(HeaderViewModel(StringIdHelper.Id(R.string.home_repositories)))
                    reposGroup.update(map)
                }
            }
        }

        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshUser()
            repo.refreshMyRepos()
        }
    }
}
