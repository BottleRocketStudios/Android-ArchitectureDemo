package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.fragment.navArgs
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFileFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFileFragment : BaseDataBindingFragment<RepositoryFileFragmentViewModel, RepositoryFileFragmentBinding>() {
    override val fragmentViewModel: RepositoryFileFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    val args: RepositoryFolderFragmentArgs by navArgs()

    override fun getLayoutRes(): Int = R.layout.repository_file_fragment

    override fun setupBinding(binding: RepositoryFileFragmentBinding) {
        super.setupBinding(binding)
        val repo: Repository = activityViewModel.selectedRepo.value
        val file: RepoFile = args.file
        activityViewModel.setTitle(file.path ?: "")
        activityViewModel.showToolbar(true)
        fragmentViewModel.loadFile(
            repo.workspace?.slug ?: "",
            repo.name ?: "",
            file.mimetype ?: "",
            file.commit?.hash ?: "",
            file.path ?: ""
        )
    }
}
