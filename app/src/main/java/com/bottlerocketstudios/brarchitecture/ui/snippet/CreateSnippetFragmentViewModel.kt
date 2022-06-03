package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.koin.core.component.inject

class CreateSnippetFragmentViewModel() : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    // UI
    val title = MutableStateFlow("")
    val filename = MutableStateFlow("")
    val contents = MutableStateFlow("")
    val private = MutableStateFlow(false)
    val failed: StateFlow<Boolean> = MutableStateFlow(false)
    val createEnabled: StateFlow<Boolean> = combine(title, filename, contents) { (title, filename, contents) ->
        !(title.isEmpty() || filename.isEmpty() || contents.isEmpty())
    }.groundState(false)

    // Events
    val onSuccess = MutableSharedFlow<Unit>()

    // UI Callbacks
    fun onCreateClick() {
        failed.set(false)
        launchIO {
            when (repo.createSnippet(title.value, filename.value, contents.value, private.value)) {
                is Status.Success -> onSuccess.emit(Unit)
                is Status.Failure -> failed.set(true)
            }.exhaustive
        }
    }
}
