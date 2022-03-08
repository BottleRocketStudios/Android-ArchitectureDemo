package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.navArgs
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.compose.repository.RepositoryBrowserScreen
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryBrowserFragment : BaseFragment<RepositoryBrowserViewModel>() {
    override val fragmentViewModel: RepositoryBrowserViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    private val args: RepositoryBrowserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val data = args.data

        activityViewModel.setTitle(data.folderPath ?: data.repoName)
        activityViewModel.showToolbar(true)
        fragmentViewModel.getFiles(data)

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
    private fun OuterScreenContent(viewModel: RepositoryBrowserViewModel) {
        ArchitectureDemoTheme {
            val state = viewModel.toState()
            RepositoryBrowserScreen(state = state)
        }
    }
}
