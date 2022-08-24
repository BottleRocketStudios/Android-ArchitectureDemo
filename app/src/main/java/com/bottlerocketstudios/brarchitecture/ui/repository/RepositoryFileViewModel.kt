package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

class RepositoryFileViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    val srcFile: MutableStateFlow<ByteArray?> = MutableStateFlow(null)
    val path: StateFlow<String> = MutableStateFlow("")

    fun loadFile(workspaceSlug: String, repoId: String, @Suppress("UNUSED_PARAMETER") mimetype: String, hash: String, path: String) {
        launchIO {
            val result = repo.getSourceFile(workspaceSlug, repoId, hash, path)
            when (result) {
                is Status.Success -> srcFile.value = result.data
                is Status.Failure -> handleError(R.string.error_loading_file)
            }.exhaustive

            this@RepositoryFileViewModel.path.setValue(path)
        }
    }
}
