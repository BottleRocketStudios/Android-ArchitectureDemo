package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.SnippetsFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepoFileViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFragmentDirections
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SnippetsFragment : BaseFragment<SnippetsFragmentViewModel, SnippetsFragmentBinding>() {
    override val fragmentViewModel: SnippetsFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.snippets_fragment

    override fun setupBinding(binding: SnippetsFragmentBinding) {
        super.setupBinding(binding)

        binding.apply {
            snippetList.adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.snippetGroup)
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
            snippetList.layoutManager = LinearLayoutManager(this@SnippetsFragment.activity)
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.refreshSnippets()
    }
}
