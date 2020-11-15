package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.SnippetsFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepoFileViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFragmentDirections
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SnippetsFragment : BaseFragment() {
    private val fragmentViewModel: SnippetsFragmentViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<SnippetsFragmentBinding>(inflater, R.layout.snippets_fragment, container, false).apply {
            viewModel = fragmentViewModel
            this.lifecycleOwner = this@SnippetsFragment
            fragmentViewModel.createClick.observe(viewLifecycleOwner, Observer {
                NavHostFragment.findNavController(this@SnippetsFragment).navigate(R.id.action_snippetsFragment_to_createSnippetFragment)
            })
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
        }.root
    }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.refreshSnippets()
    }
}
