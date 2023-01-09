package com.bottlerocketstudios.brarchitecture.ui.featuretoggle

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.bottlerocketstudios.brarchitecture.ui.Routes
import com.bottlerocketstudios.compose.featuretoggles.FeatureToggleScreen
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun ComposeActivity.featureTogglesComposable(navGraphBuilder: NavGraphBuilder, navController: NavController) {
    navGraphBuilder.composable(Routes.FeatureToggles) {
        val viewModel: FeatureToggleViewModel = getViewModel()
        viewModel.ConnectBaseViewModel {
            FeatureToggleScreen(state = it.toState())
        }

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (!activityViewModel.devOptionsEnabled) {
            navController.navigateUp()
        }

        controls.title = stringResource(id = R.string.feature_toggles_title)
    }
}
