package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.bottlerocketstudios.brarchitecture.ui.util.window.createDevicePostureFlow
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeActivity : ComponentActivity() {
        private val activityViewModel: ComposeActivityViewModel by viewModel()
        private val featureToggleRepository: FeatureToggleRepository by inject()

        private val booleanFeatureFlags =
                featureToggleRepository.featureToggles.value.filterIsInstance<
                        FeatureToggle.ToggleValueBoolean>()
        private val showSnippets =
                booleanFeatureFlags.find { it.name == "SHOW_SNIPPETS" }?.value ?: false
        private val showPullRequests =
                booleanFeatureFlags.find { it.name == "SHOW_PULL_REQUESTS" }?.value ?: false

        companion object {
                const val EMPTY_TOOLBAR_TITLE = " "
        }

        private val controls by lazy {
                MainWindowControlsImplementation(activityViewModel, navIntercept)
        }
        private val navIntercept: MutableState<(() -> Boolean)?> = mutableStateOf(null)

        private val devicePostureFlow = createDevicePostureFlow()

        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContent {
                        val devicePosture = devicePostureFlow.collectAsState()
                        val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
                        NavigationWrapper(widthSizeClass, devicePosture.value) {
                                navigator,
                                bottomBar ->
                                ArchApp(
                                        widthSize = widthSizeClass,
                                        activityViewModel = activityViewModel,
                                        navigator = navigator,
                                        controls = controls,
                                        navIntercept = navIntercept,
                                        bottomBar = bottomBar
                                )
                        }
                }
        }
}
