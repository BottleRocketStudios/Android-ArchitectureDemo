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

// TODO - Custom Animations for entrance of detail.   Try to use same animation spec for navigation side and visiblity

fun ComposeActivity.newSnippetsComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.NewSnippet) {
        val scope = rememberCoroutineScope()
        val vm: SnippetsViewModel = getViewModel()
        val config = LocalConfiguration.current

        val list = vm.snippets.collectAsState(initial = emptyList())
        controls.title = stringResource(id = R.string.snippets_title)
        controls.topLevel = true

        AnimatedListDetail(
            list = list.value + CreateSnippetItem ,
            keyProvider = { it.title },
            smallScreen = config.smallestScreenWidthDp < 580) {
            List { list, selected ->
                // TODO - Do we need selected here?  Perhaps to highlight item?
                SnippetsBrowserScreen(state = SnippetsBrowserScreenState(
                    // TODO - look to alter state to not require State if this works...
                    // Don't display CreateSnippetPlaceHolder
                    snippets = (list - CreateSnippetItem).asMutableState(),
                    onCreateSnippetClicked = {
                        scope.launch {
                            select("CREATE_SNIPPET_SCREEN")
                        }
                    }
                ))
            }
            Detail { model ->
                model.also {
                    if (it == CreateSnippetItem) {
                        val vm: CreateSnippetViewModel = getViewModel()
                        CreateSnippetScreen(state = vm.toState())
                        vm.onSuccess.LaunchCollection {
                            navController.navigateUp()
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

            // TODO - copy create screen to create view/edit screen.
            }
        }

        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    vm.refreshSnippets()
                }
            }

            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }

    }
}
