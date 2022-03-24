package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SnippetsFragmentViewModel(private val repo: BitbucketRepository) : BaseViewModel() {
    val snippets: Flow<List<SnippetUiModel>> = repo.snippets.map { it ->
        it.map { SnippetUiModel(title = it.title ?: "", userName = it.owner?.displayName ?: "", it.updated) } }

    fun onCreateClick() {
        navigationEvent.postValue(NavigationEvent.Action(R.id.action_snippetsFragment_to_createSnippetFragment))
    }

    fun refreshSnippets() {
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.refreshMySnippets()
        }
    }
}
