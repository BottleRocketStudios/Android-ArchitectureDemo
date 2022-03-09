package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class RepositoryFragmentViewModel(
    private val repo: BitbucketRepository,
    private val toaster: Toaster
) : BaseViewModel() {
    private val repos = repo.repos
    var selectedId: String? = null
    private val selectedRepository: StateFlow<Repository?> = MutableStateFlow(null)
    private val srcFiles: StateFlow<List<RepoFile>> = MutableStateFlow(emptyList())
    val filesGroup = Section()

    init {
        launchIO {
            repos.collect {
                selectRepository(selectedId)
            }
        }
        launchIO {
            repo.refreshMyRepos()
        }
        launchIO {
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
                    launchIO {
                        repo.getSource(workspaceSlug, repoName).handlingErrors(R.string.error_loading_repository) {
                            srcFiles.set(it)
                        }
                    }
                }
            }
        }
    }
}
