package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.compose.devoptions.DevOptionsScreen
import com.google.android.material.composethemeadapter.MdcTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Screen that manages dev options (only accessible from internal or debug builds (aka non release production builds)
 *
 * As this is a non production screen, note that user facing strings are hardcoded and text styles are manually built up vs using/referencing xml styles.
 */
@Suppress("TooManyFunctions")
class DevOptionsFragment : BaseFragment<DevOptionsViewModel>() {
    override val fragmentViewModel: DevOptionsViewModel by viewModel()
    private val buildConfigProvider by inject<BuildConfigProvider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (buildConfigProvider.isProductionReleaseBuild) {
            // NOTE: Special case usage of findNavController
            findNavController().popBackStack()
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                OuterScreenContent(viewModel = fragmentViewModel)
            }
        }
    }

    @Composable
    private fun OuterScreenContent(viewModel: DevOptionsViewModel) {
        MdcTheme {
            Surface {
                val state = viewModel.toState()
                DevOptionsScreen(state = state)
            }
        }
    }
}
