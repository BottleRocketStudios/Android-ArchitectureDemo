package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFragment : BaseFragment<RepositoryFragmentViewModel, RepositoryFragmentBinding>() {
    override val fragmentViewModel: RepositoryFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()

    override fun getLayoutRes(): Int = R.layout.repository_fragment

    override fun setupBinding(binding: RepositoryFragmentBinding) {
        super.setupBinding(binding)
        binding.apply {
            mainActivityViewModel = activityViewModel
            fileList.adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.filesGroup)
                setOnItemClickListener { item, _ ->
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

            viewLifecycleOwner.lifecycleScope.launch {
                activityViewModel.selectedRepo.collect {
                    fragmentViewModel.selectRepository(it.name)
                    activityViewModel.setTitle(it.name ?: "")
                    activityViewModel.showToolbar(true)
                }
            }
        }
    }
}
