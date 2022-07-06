package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreen
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreen
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreenState
import com.bottlerocketstudios.compose.util.LaunchCollection
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.toStringIdHelper
import com.bottlerocketstudios.compose.widgets.listdetail.AnimatedListDetail
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

// Used to represent create item in list so it can be "selected" for detail view
private val CreateSnippetItem = SnippetUiModel(
    id = "CREATE_SNIPPET_SCREEN",
    workspaceSlug = "",
    title = "CREATE_SNIPPET_SCREEN",
    userName = "UI_MODEL",
    formattedLastUpdatedTime = "".toStringIdHelper()
)

// TODO - Custom Animations for entrance of detail.   Try to use same animation spec for navigation side and visibility
fun ComposeActivity.snippetListDetailComposable(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(Routes.Snippets) {
        val scope = rememberCoroutineScope()
        val snippetsViewModel: SnippetsViewModel = getViewModel()
        val config = LocalConfiguration.current

        val list = snippetsViewModel.snippets.collectAsState(initial = emptyList())
        controls.title = stringResource(id = R.string.snippets_title)
        controls.topLevel = true

        AnimatedListDetail(
            list = list.value + CreateSnippetItem,
            keyProvider = { it.id },
            smallScreen = config.smallestScreenWidthDp < 580
        ) {

            // Define List UI and connect to VM
            List { list, selected ->
                // TODO - Use selected to highlight item
                SnippetsBrowserScreen(
                    state = SnippetsBrowserScreenState(
                        // Don't display CreateSnippetPlaceHolder
                        snippets = (list - CreateSnippetItem).asMutableState(),
                        createVisible = snippetsViewModel.showCreateCta.collectAsState(),
                        onCreateSnippetClicked = {
                            scope.launch {
                                select("CREATE_SNIPPET_SCREEN")
                            }
                        }
                    )
                )
            }

            // Define Detail UI for create, detail, and empty states
            Detail { model ->
                model?.also {
                    // Show Create snippet in detail pane when applicable
                    if (it == CreateSnippetItem) {
                        val createSnippetViewModel: CreateSnippetViewModel = getViewModel()
                        CreateSnippetScreen(state = createSnippetViewModel.toState())
                        createSnippetViewModel.onSuccess.LaunchCollection {
                            select(null)
                            snippetsViewModel.refreshSnippets()
                        }
                    } else {
                        //    TODO - Normal Detail screen.
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
                controls.topLevel = !detailShowing || config.smallestScreenWidthDp >= 580

                // If detail showing, provide app bar nav interceptor, otherwise null
                // FIXME - This isn't working as nav itercept is not stateful and triggering recompositions of AppBar with new value.
                navIntercept = if (detailShowing) ({
                    scope.launch { select(null) }
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
