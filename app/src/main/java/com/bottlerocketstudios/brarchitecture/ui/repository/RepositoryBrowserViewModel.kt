package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.repository.RepositoryItemUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class RepositoryBrowserViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    // State
    private val srcFiles = MutableStateFlow<List<RepoFile>>(emptyList())
    private var currentRepoName: String = ""

    // UI
    val path: StateFlow<String> = MutableStateFlow("")
    val itemCount: StateFlow<Int> = srcFiles
        .map { it.size }
        .groundState(0)
    val uiModels: StateFlow<List<RepositoryItemUiModel>> = srcFiles
        .map { files ->
            files.map { file ->
                RepositoryItemUiModel(
                    path = file.path ?: "",
                    size = file.size ?: 0,
                    isFolder = file.type == "commit_directory"
                )
            }
        }
        .groundState(emptyList())

    // Events
    val directoryClickedEvent = MutableSharedFlow<RepositoryBrowserData>()
    val fileClickedEvent = MutableSharedFlow<RepositoryFileData>()

    // Load Logic
    fun getFiles(data: RepositoryBrowserData) {
        currentRepoName = data.repoName
        path.setValue(data.folderPath ?: data.repoName)
        val selectedRepo = repo.repos.value.firstOrNull { it.name?.equals(data.repoName) ?: false }
        selectedRepo?.let {
            val slug = it.workspaceDto?.slug ?: ""
            val name = it.name ?: ""
            launchIO {
                val result = if (data.folderHash != null && data.folderPath != null) {
                    repo.getSourceFolder(slug, name, data.folderHash, data.folderPath)
                } else {
                    repo.getSource(slug, name)
                }
                when (result) {
                    is Status.Success -> srcFiles.value = result.data
                    is Status.Failure -> handleError(R.string.error_loading_repository)
                }.exhaustive
            }
        }
    }

    // UI Callbacks
    fun onRepoItemClicked(item: RepositoryItemUiModel) {
        srcFiles.value.firstOrNull { it.path == item.path }?.let { file ->
            launchIO {
                if (item.isFolder) {
                    directoryClickedEvent.emit(RepositoryBrowserData(
                        repoName = currentRepoName,
                        folderPath = file.path,
                        folderHash = file.commit?.hash
                    ))
                } else {
                    fileClickedEvent.emit(
                        RepositoryFileData(
                            hash = file.commit?.hash ?: "",
                            path = file.path ?: "",
                            mimeType = file.mimetype ?: "",
                        )
                    )
                }
            }
        }
    }
}
