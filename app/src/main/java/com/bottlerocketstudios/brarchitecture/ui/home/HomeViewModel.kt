package com.bottlerocketstudios.brarchitecture.ui.home

import android.app.Application
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.launch


class HomeViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val user = repo.user
    val repos = repo.repos
    val reposGroup = Section()
    
    private val repoObserver = Observer<List<Repository>> { repoList ->
        val map = repoList.map {RepositoryViewModel(it)}
        reposGroup.update(map)
    }

    init {
        repos.observeForever(repoObserver)
        launch {
            val t = repo.refreshUser()
            val p = repo.refreshMyRepos()
        }
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        repos.removeObserver(repoObserver)
    }
}
