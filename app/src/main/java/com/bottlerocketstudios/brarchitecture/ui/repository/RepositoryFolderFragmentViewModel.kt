package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class RepositoryFolderFragmentViewModel(
    private val repo: BitbucketRepository,
    private val toaster: Toaster,
) : BaseViewModel() {
    private val srcFiles: StateFlow<List<RepoFile>> = MutableStateFlow(emptyList())
    val filesGroup = Section()
    var path: String? = null

    init {
        launchIO {
            srcFiles.collect { files ->
                val map = files.map { RepoFileViewModel(it) }
                withContext(dispatcherProvider.Main) {
                    filesGroup.setHeader(FolderHeaderViewModel(path ?: "", files.size))
                    filesGroup.update(map)
                }
            }
        }
    }

    fun loadRepo(workspaceSlug: String, repoId: String, hash: String, path: String) {
        launchIO {
            val result = repo.getSourceFolder(workspaceSlug, repoId, hash, path)
            when (result) {
                is Status.Success -> {
                    srcFiles.set(result.data)
                    this@RepositoryFolderFragmentViewModel.path = path
                }
                is Status.Failure -> {
                    // TODO: Improve error messaging
                    withContext(dispatcherProvider.Main) {
                        toaster.toast(R.string.error_loading_folder)
                    }
                }
            }.exhaustive
        }
    }
}
