package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch

class RepositoryFileFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val srcFile: LiveData<String?> = MutableLiveData()
    val path: LiveData<String?> = MutableLiveData()
    fun loadFile(workspaceSlug: String, repoId: String, @Suppress("UNUSED_PARAMETER") mimetype: String, hash: String, path: String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            // TODO: Differentiate UI per type of content (ex: image/text/etc)
            srcFile.postValue(repo.getSourceFile(workspaceSlug, repoId, hash, path))
            this@RepositoryFileFragmentViewModel.path.postValue(path)
        }
    }
}
