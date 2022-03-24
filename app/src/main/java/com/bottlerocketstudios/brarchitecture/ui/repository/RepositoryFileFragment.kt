package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.compose.repository.FileBrowserScreen
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFileFragment : BaseFragment<RepositoryFileFragmentViewModel>() {
    override val fragmentViewModel: RepositoryFileFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    private val args: RepositoryFileFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            loadFileContent()
            FileBrowserScreen(state = fragmentViewModel.toState())
        }

    private fun loadFileContent() {
        val repo: GitRepository = activityViewModel.selectedRepo.value
        val data: RepositoryFileData = args.data

        activityViewModel.title.value = data.path
        activityViewModel.showToolbar.value = true
        fragmentViewModel.loadFile(
            repo.workspace?.slug ?: "",
            repo.name ?: "",
            data.mimeType,
            data.hash,
            data.path
        )
    }
}
