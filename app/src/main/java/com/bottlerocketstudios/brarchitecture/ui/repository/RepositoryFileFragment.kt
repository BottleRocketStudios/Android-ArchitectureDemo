package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.fragment.navArgs
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFileFragmentBinding
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFileFragment : BaseDataBindingFragment<RepositoryFileFragmentViewModel, RepositoryFileFragmentBinding>() {
    override val fragmentViewModel: RepositoryFileFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    val args: RepositoryFileFragmentArgs by navArgs()

    override fun getLayoutRes(): Int = R.layout.repository_file_fragment

    override fun setupBinding(binding: RepositoryFileFragmentBinding) {
        super.setupBinding(binding)
        val repo: Repository = activityViewModel.selectedRepo.value
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
