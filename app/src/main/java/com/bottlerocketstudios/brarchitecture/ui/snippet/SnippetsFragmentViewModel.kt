package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import java.time.Clock

class SnippetsFragmentViewModel(private val repo: BitbucketRepository) : BaseViewModel() {
    private val clock by inject<Clock>()
    val snippets: Flow<List<SnippetUiModel>> = repo.snippets.map { it ->
        it.map {
            SnippetUiModel(
                title = it.title ?: "",
                userName = it.owner?.displayName ?: "",
                formattedTime = it.updated.formattedUpdateTime(clock = clock)
            )
        }
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
