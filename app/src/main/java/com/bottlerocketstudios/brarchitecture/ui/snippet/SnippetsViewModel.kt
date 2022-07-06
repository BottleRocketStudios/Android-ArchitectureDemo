package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import java.time.Clock

class SnippetsViewModel : BaseViewModel() {
    // DI
    private val clock by inject<Clock>()
    private val repo: BitbucketRepository by inject()

    // UI
    val snippets: Flow<List<SnippetUiModel>> = repo.snippets.map { it ->
        it.map {
            SnippetUiModel(
                id = it.id ?: "",
                workspaceSlug = it.workspace?.slug ?: "",
                title = it.title ?: "",
                userName = it.owner?.displayName ?: "",
                formattedLastUpdatedTime = it.updated.formattedUpdateTime(clock = clock)
            )
        }
    }

    val showCreateCTA = MutableStateFlow(true)

    // Events
    val createClicked = MutableSharedFlow<Unit>()

    fun onCreateClick() {
        launchIO {
            createClicked.emit(Unit)
        }
    }

    fun refreshSnippets() {
        launchIO {
            repo.refreshMySnippets()
        }
    }
}
