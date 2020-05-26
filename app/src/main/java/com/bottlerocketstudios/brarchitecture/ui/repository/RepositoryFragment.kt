package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class RepositoryFragment : BaseFragment() {
    private val fragmentViewModel: RepositoryFragmentViewModel by lazy {
        getProvidedViewModel(RepositoryFragmentViewModel::class.java)
    }

    private val activityViewModel: MainActivityViewModel by lazy {
        getProvidedActivityViewModel(MainActivityViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<RepositoryFragmentBinding>(inflater, R.layout.repository_fragment, container, false).apply {
            viewModel = fragmentViewModel
            mainActivityViewModel = activityViewModel
            setLifecycleOwner(this@RepositoryFragment)
            fileList.adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.filesGroup)
                setOnItemClickListener { item, view ->
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type == "commit_directory") {
                        val action = RepositoryFragmentDirections.actionRepositoryFragmentToRepositoryFolderFragment(item.viewModel.file)
                        Navigation.findNavController(root).navigate(action)
                    }
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type == "commit_file") {
                        val action = RepositoryFragmentDirections.actionRepositoryFragmentToRepositoryFileFragment(item.viewModel.file)
                        Navigation.findNavController(root).navigate(action)
                    }
                }
            }
            fileList.layoutManager = LinearLayoutManager(this@RepositoryFragment.activity)
            activityViewModel.selectedRepo.observe(viewLifecycleOwner, Observer {
                fragmentViewModel.selectRepository(it.name)
            })
        }.root
    }
}
