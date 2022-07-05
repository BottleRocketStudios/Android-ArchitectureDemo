package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.activity.compose.BackHandler
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
import androidx.navigation.NavController
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

private val CreateSnippetItem = SnippetUiModel(
    title = "CREATE_SNIPPET_SCREEN",
    userName = "UI_MODEL",
    formattedLastUpdatedTime = "".toStringIdHelper()
)

// TODO - Custom Animations for entrance of detail.   Try to use same animation spec for navigation side and visibility

fun ComposeActivity.newSnippetsComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.NewSnippet) {
        val scope = rememberCoroutineScope()
        val snippetsViewModel: SnippetsViewModel = getViewModel()
        val config = LocalConfiguration.current

        val list = snippetsViewModel.snippets.collectAsState(initial = emptyList())
        controls.title = stringResource(id = R.string.snippets_title)
        controls.topLevel = true

        AnimatedListDetail(
            list = list.value + CreateSnippetItem ,
            // FIXME - Use unique ID
            keyProvider = { it.title },
            smallScreen = config.smallestScreenWidthDp < 580) {
            List { list, selected ->
                // TODO - Use selected to highlight item
                SnippetsBrowserScreen(state = SnippetsBrowserScreenState(
                    // Don't display CreateSnippetPlaceHolder
                    snippets = (list - CreateSnippetItem).asMutableState(),
                    createVisible = snippetsViewModel.showCreateCTA.collectAsState(),
                    onCreateSnippetClicked = {
                        scope.launch {
                            select("CREATE_SNIPPET_SCREEN")
                        }
                    }
                ))
            }
            Detail { model ->
                model.also {
                    // Show Create snippet in Detail when applicable
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
                } ?: run  {
                    // Empty Detail State
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray))
                }
            }

            DetailState { detailShowing ->
                // Control list FAB visibility based of detail content.
                snippetsViewModel.showCreateCTA.value = !detailShowing
                // Show back arrow when detail is showing on small devices.
                controls.topLevel = !detailShowing || config.smallestScreenWidthDp >= 580
            }
        }

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
