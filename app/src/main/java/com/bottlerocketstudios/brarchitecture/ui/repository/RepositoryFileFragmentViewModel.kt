package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch

class RepositoryFileFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    private val _srcFile = MutableLiveData<String?>()
    val srcFile: LiveData<String?> = _srcFile
    fun loadFile(owner: String, repoId: String, @Suppress("UNUSED_PARAMETER") mimetype: String, hash: String, path: String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            // TODO: Differentiate UI per type of content (ex: image/text/etc)
            _srcFile.postValue(repo.getSourceFile(owner, repoId, hash, path))
        }
    }
}
