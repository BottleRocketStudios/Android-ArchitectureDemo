package com.bottlerocketstudios.brarchitecture.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.HeaderViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.hadilq.liveevent.LiveEvent
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class HomeViewModel(app: Application, repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val user = repo.user
    val repos = repo.repos
    val reposGroup = Section()
    val userClick = LiveEvent<Boolean>()

    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild
    private val _devOptionsClicked = LiveEvent<Unit>()
    val devOptionsClicked: LiveData<Unit> = _devOptionsClicked

    private val repoObserver = Observer<List<Repository>> { repoList ->
        val map = repoList.map { RepositoryViewModel(it) }
        reposGroup.setHeader(HeaderViewModel(R.string.home_repositories))
        reposGroup.update(map)
    }

    init {
        repos.observeForever(repoObserver)
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshUser()
            repo.refreshMyRepos()
        }
    }

    fun doTheThing() {
        userClick.value = true
    }

    fun onDevOptionsClicked() {
        _devOptionsClicked.postValue(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        repos.removeObserver(repoObserver)
    }
}
