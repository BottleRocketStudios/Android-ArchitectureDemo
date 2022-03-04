package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.xwray.groupie.Section
import kotlinx.coroutines.withContext

class SnippetsFragmentViewModel(private val repo: BitbucketRepository) : BaseViewModel() {
    private val snippets = repo.snippets
    val snippetGroup = Section()

    init {
        launchIO {
            snippets.collect { snippetList ->
                val map = snippetList.map { SnippetViewModel(it) }
                withContext(dispatcherProvider.Main) {
                    snippetGroup.update(map)
                }
            }
        }
        refreshSnippets()
    }

    fun onCreateClick() {
        navigationEvent.postValue(NavigationEvent.Action(R.id.action_snippetsFragment_to_createSnippetFragment))
    }

    fun refreshSnippets() {
        launchIO {
            repo.refreshMySnippets()
        }
    }
}
