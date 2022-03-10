package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.compose.repository.RepositoryBrowserScreen
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

        return ComposeScreen {
            RepositoryBrowserScreen(state = fragmentViewModel.toState())
        }
    }
}
