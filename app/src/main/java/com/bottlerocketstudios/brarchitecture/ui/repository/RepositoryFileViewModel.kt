package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

class RepositoryFileViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    // endregion

    // region UI State
    val srcFile: MutableStateFlow<ByteArray?> = MutableStateFlow(null)
    val path: StateFlow<String> = MutableStateFlow("")
    // endregion

    // region UI Callbacks

    fun loadFile(
            workspaceSlug: String,
            repoId: String,
            @Suppress("UNUSED_PARAMETER") mimetype: String,
            hash: String,
            path: String
    ) {
        launchIO {
            val result = repo.getSourceFile(workspaceSlug, repoId, hash, path)
            result
                    .onSuccess { srcFile.value = it }
                    .onFailureLogged(errorStrId = R.string.error_loading_file)

            this@RepositoryFileViewModel.path.setValue(path)
        }
    }
    // endregion
}
