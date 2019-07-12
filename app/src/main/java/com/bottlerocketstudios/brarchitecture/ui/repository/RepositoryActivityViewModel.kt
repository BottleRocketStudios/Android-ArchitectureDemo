package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import kotlinx.coroutines.launch


class RepositoryActivityViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val repos = repo.repos
    var selectedId: String? = null
    val _selectedRepository = MutableLiveData<Repository>()
    val selectedRepository: LiveData<Repository>
        get() = _selectedRepository
    
    fun selectRepository(id: String?) {
        selectedId = id
        repos.value?.firstOrNull{it.name?.equals(id)?:false}?.let {
            _selectedRepository.value = it
        }
    }

    private val repoObserver = Observer<List<Repository>> { repoList ->
        selectRepository(selectedId)
    }

    init {
        repos.observeForever(repoObserver)
        launch {
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

