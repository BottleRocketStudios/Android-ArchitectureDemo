package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFileFragmentBinding
import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFileFragment : BaseFragment() {
    private val fragmentViewModel: RepositoryFileFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    val args: RepositoryFolderFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<RepositoryFileFragmentBinding>(
            inflater,
            R.layout.repository_file_fragment,
            container,
            false
        ).apply {
            viewModel = fragmentViewModel
            val repo: Repository = activityViewModel.selectedRepo.value ?: return root
            val file: RepoFile = args.file
            fragmentViewModel.loadFile(
                repo.owner?.nickname ?: "",
                repo.name ?: "",
                file.mimetype ?: "",
                file.commit?.hash ?: "",
                file.path ?: "")
            lifecycleOwner = this@RepositoryFileFragment
        }.root
    }
}
