package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.koin.core.component.inject

class CreateSnippetViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    // endregion

    // region UI State
    val title = MutableStateFlow("")
    val filename = MutableStateFlow("")
    val contents = MutableStateFlow("")
    val private = MutableStateFlow(false)
    val failed: StateFlow<Boolean> = MutableStateFlow(false)
    val createEnabled: StateFlow<Boolean> =
            combine(title, filename, contents) { (title, filename, contents) ->
                        !(title.isEmpty() || filename.isEmpty() || contents.isEmpty())
                    }
                    .groundState(false)
    // endregion

    // region Events
    val onSuccess = MutableSharedFlow<Unit>()
    // endregion

    // region UI Callbacks
    fun onCreateClick() {
        failed.setValue(false)
        launchIO {
            when (repo.createSnippet(title.value, filename.value, contents.value, private.value)) {
                is Status.Success -> onSuccess.emit(Unit)
                is Status.Failure -> failed.setValue(true)
            }
        }
    }
    // endregion
}
