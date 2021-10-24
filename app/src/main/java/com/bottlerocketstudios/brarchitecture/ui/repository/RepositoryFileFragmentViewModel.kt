package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryFileFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val toaster: Toaster, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val srcFile: StateFlow<String> = MutableStateFlow("")
    val path: StateFlow<String> = MutableStateFlow("")
    fun loadFile(workspaceSlug: String, repoId: String, @Suppress("UNUSED_PARAMETER") mimetype: String, hash: String, path: String) {
        viewModelScope.launch(dispatcherProvider.IO) {
            val result = repo.getSourceFile(workspaceSlug, repoId, hash, path)
            when (result) {
                is ApiResult.Success -> srcFile.set(result.data) // TODO: Differentiate UI per type of content (ex: image/text/etc)
                is ApiResult.Failure -> {
                    // TODO: Improve error messaging
                    withContext(dispatcherProvider.Main) {
                        toaster.toast(R.string.error_loading_file)
                    }
                }
            }.exhaustive

            this@RepositoryFileFragmentViewModel.path.set(path)
        }
    }
}
