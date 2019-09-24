package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFolderFragmentBinding
import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class RepositoryFolderFragment : BaseFragment() {
    private val fragmentViewModel: RepositoryFolderFragmentViewModel by lazy {
        getProvidedViewModel(RepositoryFolderFragmentViewModel::class.java)
    }

    private val activityViewModel: MainActivityViewModel by lazy {
        getProvidedActivityViewModel(MainActivityViewModel::class.java)
    }
    
    val args: RepositoryFolderFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<RepositoryFolderFragmentBinding>(
            inflater,
            R.layout.repository_folder_fragment,
            container,
            false
        ).apply {
            viewModel = fragmentViewModel
            val repo: Repository = activityViewModel.selectedRepo.value ?: return root
            val folder: RepoFile = args.file
            fileList.adapter = GroupAdapter<ViewHolder>().apply {
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
                repo.owner?.nickname ?: "",
                repo.name ?: "",
                folder.commit?.hash ?: "",
                folder.path ?: ""
            )
            setLifecycleOwner(this@RepositoryFolderFragment)
        }.root
    }
}
