package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val toaster: Toaster, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val repos = repo.repos
    var selectedId: String? = null
    val selectedRepository: LiveData<Repository?> = MutableLiveData()
    val srcFiles: LiveData<List<RepoFile>?> = MutableLiveData()
    val filesGroup = Section()

    fun selectRepository(id: String?) {
        selectedId = id
        repos.value?.firstOrNull { it.name?.equals(id) ?: false }?.let {
            selectedRepository.set(it)
            it.workspace?.slug?.let { workspaceSlug ->
                it.name?.let { repoName ->
                    viewModelScope.launch(dispatcherProvider.IO) {
                        val result = repo.getSource(workspaceSlug, repoName)
                        when (result) {
                            is ApiResult.Success -> srcFiles.postValue(result.data)
                            is ApiResult.Failure -> {
                                // TODO: Improve error messaging
                                withContext(dispatcherProvider.Main) {
                                    toaster.toast(R.string.error_loading_repository)
                                }
                            }
                        }.exhaustive
                    }
                }
            }
        }
    }

    private val filesObserver = Observer<List<RepoFile>?> { files ->
        val map = files?.map { RepoFileViewModel(it) }
        map?.let {
            filesGroup.setHeader(FolderHeaderViewModel(selectedRepository.value?.name ?: "", it.size))
            filesGroup.update(map)
        }
    }

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repos.collect {
                selectRepository(selectedId)
            }
        }
        srcFiles.observeForever(filesObserver)
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshMyRepos()
        }
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    private fun doClear() {
        srcFiles.removeObserver(filesObserver)
    }
}
