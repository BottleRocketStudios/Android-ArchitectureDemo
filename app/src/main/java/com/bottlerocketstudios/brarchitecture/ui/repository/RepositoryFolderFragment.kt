package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFolderFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFolderFragment : BaseDataBindingFragment<RepositoryFolderFragmentViewModel, RepositoryFolderFragmentBinding>() {
    override val fragmentViewModel: RepositoryFolderFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    val args: RepositoryFolderFragmentArgs by navArgs()

    override fun getLayoutRes(): Int = R.layout.repository_folder_fragment

    override fun setupBinding(binding: RepositoryFolderFragmentBinding) {
        super.setupBinding(binding)

        binding.apply {
            val repo: Repository = activityViewModel.selectedRepo.value
            val folder: RepoFile = args.file
            activityViewModel.setTitle(folder.path ?: "")
            activityViewModel.showToolbar(true)
            fileList.adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.filesGroup)
                /*
                setOnItemClickListener { item, view ->
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type == "commit_directory") {
                        startActivity(
                            RepositoryFolderFragment.newIntent(
                                this@RepositoryFolderFragment,
                                repo,
                                item.viewModel.file
                            )
                        )
                    }
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type == "commit_file") {
                        startActivity(
                            RepositoryFileActivity.newIntent(
                                this@RepositoryFolderFragment,
                                repo,
                                item.viewModel.file
                            )
                        )
                    }
                }*/
            }
            fileList.layoutManager = LinearLayoutManager(this@RepositoryFolderFragment.activity)

            fragmentViewModel.loadRepo(
                repo.workspace?.slug ?: "",
                repo.name ?: "",
                folder.commit?.hash ?: "",
                folder.path ?: ""
            )
        }
    }
}
