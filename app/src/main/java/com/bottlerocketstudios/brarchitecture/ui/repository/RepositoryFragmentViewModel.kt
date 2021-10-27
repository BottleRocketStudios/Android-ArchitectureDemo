package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val toaster: Toaster, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val repos = repo.repos
    var selectedId: String? = null
    val selectedRepository: StateFlow<Repository?> = MutableStateFlow(null)
    val srcFiles: StateFlow<List<RepoFile>> = MutableStateFlow(emptyList())
    val filesGroup = Section()

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repos.collect {
                selectRepository(selectedId)
            }
        }
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshMyRepos()
        }
        viewModelScope.launch(dispatcherProvider.IO) {
            srcFiles.collect { files ->
                val map = files.map { RepoFileViewModel(it) }
                withContext(dispatcherProvider.Main) {
                    filesGroup.setHeader(FolderHeaderViewModel(selectedRepository.value?.name ?: "", map.size))
                    filesGroup.update(map)
                }
            }
        }
    }

    fun selectRepository(id: String?) {
        selectedId = id
        repos.value.firstOrNull { it.name?.equals(id) ?: false }?.let {
            selectedRepository.setNullable(it)
            it.workspace?.slug?.let { workspaceSlug ->
                it.name?.let { repoName ->
                    viewModelScope.launch(dispatcherProvider.IO) {
                        val result = repo.getSource(workspaceSlug, repoName)
                        when (result) {
                            is ApiResult.Success -> srcFiles.set(result.data)
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
}
