package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class RepositoryFolderFragmentViewModel (app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val _srcFiles = MutableLiveData<List<RepoFile>?>()
    val srcFiles: LiveData<List<RepoFile>?>
        get() = _srcFiles
    val filesGroup = Section()

    private val filesObserver = Observer<List<RepoFile>?> { files ->
        val map = files?.map { RepoFileViewModel(it) }
        map?.let {
            filesGroup.update(map)
        }
    }

    init {
        srcFiles.observeForever(filesObserver)
    }
    
    fun loadRepo(owner: String, repoId: String, hash: String, path: String) {
        launch {
            _srcFiles.postValue(repo.getSourceFolder(owner, repoId, hash, path))
        }
    }

    override fun onCleared() {
        super.onCleared()
        srcFiles.removeObserver(filesObserver)
    }
}
