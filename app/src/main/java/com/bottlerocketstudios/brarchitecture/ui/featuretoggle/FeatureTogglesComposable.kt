package com.bottlerocketstudios.brarchitecture.ui.featuretoggle

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.MainWindowControls
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.featuretoggles.FeatureToggleScreen
import org.koin.androidx.compose.getViewModel

fun NavGraphBuilder.featureTogglesComposable(navController: NavController, controls: MainWindowControls) {
    composable(Routes.FeatureToggles) {
        val viewModel: FeatureToggleViewModel = getViewModel()
        val activityViewModel: ComposeActivityViewModel = getViewModel()

        FeatureToggleScreen(state = viewModel.toState())

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (!activityViewModel.devOptionsEnabled) {
            navController.navigateUp()
        }

        // Update top level controls
        controls.reset()
        controls.title = stringResource(id = R.string.feature_toggles_title)
    }
}
