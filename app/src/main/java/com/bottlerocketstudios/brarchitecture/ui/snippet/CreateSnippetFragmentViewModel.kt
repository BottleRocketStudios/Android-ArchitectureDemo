package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateSnippetFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val title = MutableLiveData<String>()
    val filename = MutableLiveData<String>()
    val contents = MutableLiveData<String>()
    val private = MutableLiveData<Boolean>()
    private val _createEnabled = MutableLiveData<Boolean>()
    val createEnabled: LiveData<Boolean> = _createEnabled
    private val _done = LiveEvent<Unit>()
    val done: LiveData<Unit> = _done
    private val _failed = MutableLiveData<Boolean>()
    val failed: LiveData<Boolean> = _failed

    val textWatcher = Observer<String> { _ ->
        _createEnabled.postValue(!(title.value.isNullOrEmpty()||filename.value.isNullOrEmpty()||contents.value.isNullOrEmpty()))
    }

    init {
        title.observeForever(textWatcher)
        filename.observeForever(textWatcher)
        contents.observeForever(textWatcher)
    }

    fun onCreateClick() {
        _failed.postValue(false)
        repo.user.value?.uuid?.let { userString ->
            title.value?.let { titleString ->
                filename.value?.let { filenameString ->
                    contents.value?.let { contentsString ->
                        viewModelScope.launch(dispatcherProvider.IO) {
                            val result = repo.createSnippet(userString, titleString, filenameString, contentsString, private.value ?: false)
                            if (result) {
                                _done.postValue(Unit)
                            } else {
                                _failed.postValue(true)
                            }
                        }
                    } ?: Timber.e("Snippet creation failed because contents was unexpectedly null")
                } ?: Timber.e("Snippet creation failed because filename was unexpectedly null")
            } ?: Timber.e("Snippet creation failed because title was unexpectedly null")
        } ?: Timber.e("Snippet creation failed because user uuid was unexpectedly null")
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        title.removeObserver(textWatcher)
        filename.removeObserver(textWatcher)
        contents.removeObserver(textWatcher)
    }
}
