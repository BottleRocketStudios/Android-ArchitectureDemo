package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryFileFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val toaster: Toaster, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val srcFile: LiveData<String?> = MutableLiveData()
    val path: LiveData<String?> = MutableLiveData()
    fun loadFile(workspaceSlug: String, repoId: String, @Suppress("UNUSED_PARAMETER") mimetype: String, hash: String, path: String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            val result = repo.getSourceFile(workspaceSlug, repoId, hash, path)
            when (result) {
                is ApiResult.Success -> srcFile.postValue(result.data) // TODO: Differentiate UI per type of content (ex: image/text/etc)
                is ApiResult.Failure -> {
                    // TODO: Improve error messaging
                    withContext(dispatcherProvider.Main) {
                        toaster.toast(R.string.error_loading_file)
                    }
                }
            }.exhaustive

            this@RepositoryFileFragmentViewModel.path.postValue(path)
        }
    }
}
