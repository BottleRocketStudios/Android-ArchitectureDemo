package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.hadilq.liveevent.LiveEvent
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class SnippetsFragmentViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val snippets = repo.snippets
    val snippetGroup = Section()
    private val _createClick = LiveEvent<Unit>()
    val createClick: LiveData<Unit> = _createClick

    private val snippetObserver = Observer<List<Snippet>> { snippetList ->
        val map = snippetList.map { SnippetViewModel(it) }
        snippetGroup.update(map)
    }

    init {
        snippets.observeForever(snippetObserver)
        refreshSnippets()
    }

    fun onCreateClick() {
        _createClick.postValue(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        snippets.removeObserver(snippetObserver)
    }

    fun refreshSnippets() {
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshMySnippets()
        }
    }
}
