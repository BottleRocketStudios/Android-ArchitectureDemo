package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class RepositoryFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val repos = repo.repos
    var selectedId: String? = null
    private val _selectedRepository = MutableLiveData<Repository?>()
    val selectedRepository: LiveData<Repository?> = _selectedRepository
    private val _srcFiles = MutableLiveData<List<RepoFile>?>()
    val srcFiles: LiveData<List<RepoFile>?> = _srcFiles
    val filesGroup = Section()

    fun selectRepository(id: String?) {
        selectedId = id
        repos.value?.firstOrNull { it.name?.equals(id) ?: false }?.let {
            _selectedRepository.value = it
            it.owner?.nickname?.let { nickname ->
                it.name?.let { repoName ->
                    viewModelScope.launch(dispatcherProvider.IO) {
                        _srcFiles.postValue(repo.getSource(nickname, repoName))
                    }
                }
            }
        }
    }

    private val repoObserver = Observer<List<Repository>> { _ ->
        selectRepository(selectedId)
    }

    private val filesObserver = Observer<List<RepoFile>?> { files ->
        val map = files?.map { RepoFileViewModel(it) }
        map?.let {
            filesGroup.update(map)
        }
    }

    init {
        repos.observeForever(repoObserver)
        srcFiles.observeForever(filesObserver)
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshMyRepos()
        }
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        repos.removeObserver(repoObserver)
        srcFiles.removeObserver(filesObserver)
    }
}
