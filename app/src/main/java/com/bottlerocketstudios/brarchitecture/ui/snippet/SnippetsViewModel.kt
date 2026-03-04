package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import com.bottlerocketstudios.compose.util.toStringIdHelper
import java.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class SnippetsViewModel : BaseViewModel() {
    // region DI
    private val clock by inject<Clock>()
    private val repo: BitbucketRepository by inject()
    // endregion

    // region UI State
    val snippets: Flow<List<SnippetUiModel>> =
            repo.snippets.map { snippets ->
                snippets.map { snippet ->
                    SnippetUiModel(
                            id = snippet.id ?: "",
                            workspaceId = snippet.workspace?.slug ?: snippet.workspace?.uuid ?: "",
                            title = snippet.title ?: "",
                            userName = snippet.owner?.displayName ?: "",
                            formattedLastUpdatedTime =
                                    snippet.updated.formattedUpdateTime(clock = clock)
                    )
                }
            }

    val snippetClick: MutableSharedFlow<SnippetUiModel> = MutableSharedFlow()
    val showCreateCta = MutableStateFlow(true)
    // endregion

    // region UI Callbacks

    fun refreshSnippets() {
        launchIO {
            showLoadingIndicator.wrapIndicator {
                repo.refreshMySnippets()
            }
        }
    }

    fun onSnippetClick(snippet: SnippetUiModel) {
        launchIO { snippetClick.emit(snippet) }
    }
    // endregion

    companion object {
        val CreateSnippetItem =
                SnippetUiModel(
                        id = "CREATE_SNIPPET_SCREEN",
                        workspaceId = "",
                        title = "CREATE_SNIPPET_SCREEN",
                        userName = "UI_MODEL",
                        formattedLastUpdatedTime = "".toStringIdHelper()
                )
    }
}
