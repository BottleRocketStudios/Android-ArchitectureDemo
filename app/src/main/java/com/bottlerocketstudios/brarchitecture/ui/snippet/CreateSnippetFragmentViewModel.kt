package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateSnippetFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val title = MutableLiveData<String>()
    val filename = MutableLiveData<String>()
    val contents = MutableLiveData<String>()
    val private = MutableLiveData<Boolean>()
    val createEnabled: LiveData<Boolean> = MutableLiveData()
    val failed: LiveData<Boolean> = MutableLiveData()

    val textWatcher = Observer<String> { _ ->
        createEnabled.postValue(!(title.value.isNullOrEmpty() || filename.value.isNullOrEmpty() || contents.value.isNullOrEmpty()))
    }

    init {
        title.observeForever(textWatcher)
        filename.observeForever(textWatcher)
        contents.observeForever(textWatcher)
    }

    fun onCreateClick() {
        failed.postValue(false)
        title.value?.let { titleString ->
            filename.value?.let { filenameString ->
                contents.value?.let { contentsString ->
                    viewModelScope.launch(dispatcherProvider.IO) {
                        val result = repo.createSnippet(titleString, filenameString, contentsString, private.value ?: false)
                        when (result) {
                            is ApiResult.Success -> navigationEvent.postValue(NavigationEvent.Up)
                            is ApiResult.Failure -> failed.postValue(true)
                        }.exhaustive
                    }
                } ?: Timber.e("Snippet creation failed because contents was unexpectedly null")
            } ?: Timber.e("Snippet creation failed because filename was unexpectedly null")
        } ?: Timber.e("Snippet creation failed because title was unexpectedly null")
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    private fun doClear() {
        title.removeObserver(textWatcher)
        filename.removeObserver(textWatcher)
        contents.removeObserver(textWatcher)
    }
}
