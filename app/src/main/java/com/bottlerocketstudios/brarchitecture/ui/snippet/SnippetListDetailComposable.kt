package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.MainWindowControls
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreen
import com.bottlerocketstudios.compose.snippets.SnippetDetailsScreen
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreen
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreenState
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.toStringIdHelper
import com.bottlerocketstudios.launchpad.compose.util.LaunchCollection
import com.bottlerocketstudios.launchpad.compose.widgets.listdetail.AnimatedListDetail
import org.koin.androidx.compose.getViewModel

// Used to represent create item in list so it can be "selected" for detail view
private val CreateSnippetItem = SnippetUiModel(
    id = "CREATE_SNIPPET_SCREEN",
    workspaceId = "",
    title = "CREATE_SNIPPET_SCREEN",
    userName = "UI_MODEL",
    formattedLastUpdatedTime = "".toStringIdHelper()
)

@Suppress("LongMethod")
fun NavGraphBuilder.snippetListDetailComposable(controls: MainWindowControls, widthSize: WindowWidthSizeClass) {
    composable(Routes.Snippets) {
        val snippetsViewModel: SnippetsViewModel = getViewModel()
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.snippets_title)
        controls.topLevel = true

        val list = snippetsViewModel.snippets.collectAsState(initial = emptyList())
        AnimatedListDetail(
            list = list.value + CreateSnippetItem,
            keyProvider = { it.id },
            compactWidth = widthSize == WindowWidthSizeClass.Compact
        ) {

            // Define List UI and connect to VM
            List { list ->
                SnippetsBrowserScreen(
                    state = SnippetsBrowserScreenState(
                        // Don't display CreateSnippetPlaceHolder
                        snippets = (list - CreateSnippetItem).asMutableState(),
                        createVisible = snippetsViewModel.showCreateCta.collectAsState(),
                        onCreateSnippetClicked = {
                            select("CREATE_SNIPPET_SCREEN")
                        },
                        onSnippetClick = {
                            select(it.id)
                        }
                    )
                )
            }

            // Define Detail UI for create, detail, and empty states
            Detail { model ->
                model?.also { snippetUiModel ->
                    // Show Create snippet in detail pane when applicable
                    if (snippetUiModel == CreateSnippetItem) {
                        val createSnippetViewModel: CreateSnippetViewModel = getViewModel()
                        CreateSnippetScreen(state = createSnippetViewModel.toState())
                        createSnippetViewModel.onSuccess.LaunchCollection {
                            select(null)
                            snippetsViewModel.refreshSnippets()
                        }
                    } else {
                        val snippetDetailsViewModel: SnippetDetailsViewModel = getViewModel()
                        SnippetDetailsScreen(state = snippetDetailsViewModel.toState())
                        snippetDetailsViewModel.getSnippetDetails(snippetUiModel)
                    }
                } ?: run {
                    // Empty Detail State
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                    )
                }
            }

            // Callback with boolean if detail is showing or not.
            DetailState { detailShowing ->
                // Control list FAB visibility based of detail content.
                snippetsViewModel.showCreateCta.value = !detailShowing

                // Show back arrow when detail is showing on small devices.
                controls.topLevel = !detailShowing || widthSize == WindowWidthSizeClass.Compact

                // If detail showing, provide app bar nav interceptor, otherwise null
                controls.navIntercept =
                    if (detailShowing) ({
                        select(null)
                        true
                    }) else null
            }
        }

        // Refresh snippet list when lifecycle resumes.
        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    snippetsViewModel.refreshSnippets()
                }
            }

            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }
    }
}
