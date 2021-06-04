package com.bottlerocketstudios.brarchitecture.ui.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.HeaderViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(app: Application, repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val user = repo.user
    val repos = repo.repos
    val reposGroup = Section()

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repos.collect { repoList ->
                withContext(dispatcherProvider.Main) {
                    val map = repoList.map { RepositoryViewModel(it) }
                    reposGroup.setHeader(HeaderViewModel(R.string.home_repositories))
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
