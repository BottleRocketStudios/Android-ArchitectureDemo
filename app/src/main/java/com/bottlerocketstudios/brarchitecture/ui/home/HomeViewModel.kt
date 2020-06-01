package com.bottlerocketstudios.brarchitecture.ui.home

import android.app.Application
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.hadilq.liveevent.LiveEvent
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class HomeViewModel(app: Application, repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val user = repo.user
    val repos = repo.repos
    val reposGroup = Section()
    val userClick = LiveEvent<Boolean>()

    private val repoObserver = Observer<List<Repository>> { repoList ->
        val map = repoList.map { RepositoryViewModel(it) }
        reposGroup.update(map)
    }

    init {
        repos.observeForever(repoObserver)
        viewModelScope.launch(dispatcherProvider.IO) {
            val t = repo.refreshUser()
            val p = repo.refreshMyRepos()
        }
    }

    fun doTheThing() {
        userClick.value = true
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        repos.removeObserver(repoObserver)
    }
}
