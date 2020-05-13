package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import kotlinx.coroutines.launch

class RepositoryFileFragmentViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val _srcFile = MutableLiveData<String?>()
    val srcFile: LiveData<String?>
        get() = _srcFile
    fun loadFile(owner: String, repoId: String, mimetype: String, hash: String, path: String) {
        launch {
            _srcFile.postValue(repo.getSourceFile(owner, repoId, hash, path))
        }
    }
}
