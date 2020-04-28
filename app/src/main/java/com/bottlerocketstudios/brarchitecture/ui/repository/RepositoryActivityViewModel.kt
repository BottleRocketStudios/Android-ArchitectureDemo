package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class RepositoryActivityViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val repos = repo.repos
    var selectedId: String? = null
    val _selectedRepository = MutableLiveData<Repository?>()
    val selectedRepository: LiveData<Repository?>
        get() = _selectedRepository
    val _srcFiles = MutableLiveData<List<RepoFile>?>()
    val srcFiles: LiveData<List<RepoFile>?>
        get() = _srcFiles
    val filesGroup = Section()

    fun selectRepository(id: String?) {
        selectedId = id
        repos.value?.firstOrNull { it.name?.equals(id) ?: false }?.let {
            _selectedRepository.value = it
            it.owner?.nickname?.let { nickname ->
                it.name?.let { repoName ->
                    launch {
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
        srcFiles.removeObserver(filesObserver)
    }
}
