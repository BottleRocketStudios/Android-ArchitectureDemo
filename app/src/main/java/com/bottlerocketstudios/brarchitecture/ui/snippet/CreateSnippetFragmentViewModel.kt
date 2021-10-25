package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CreateSnippetFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {

    // Two way databinding
    val title = MutableStateFlow("")
    val filename = MutableStateFlow("")
    val contents = MutableStateFlow("")
    val private = MutableStateFlow(false)

    // One way databinding
    val failed: StateFlow<Boolean> = MutableStateFlow(false)
    val createEnabled: StateFlow<Boolean> = combine(title, filename, contents) { (title, filename, contents) ->
        !(title.isEmpty() || filename.isEmpty() || contents.isEmpty())
    }.groundState(false)

    fun onCreateClick() {
        failed.set(false)
        title.value.let { titleString ->
            filename.value.let { filenameString ->
                contents.value.let { contentsString ->
                    viewModelScope.launch(dispatcherProvider.IO) {
                        val result = repo.createSnippet(titleString, filenameString, contentsString, private.value)
                        when (result) {
                            is ApiResult.Success -> navigationEvent.postValue(NavigationEvent.Up)
                            is ApiResult.Failure -> failed.set(true)
                        }.exhaustive
                    }
                }
            }
        }
    }
}
