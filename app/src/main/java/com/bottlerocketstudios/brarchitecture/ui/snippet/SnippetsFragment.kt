package com.bottlerocketstudios.brarchitecture.ui.snippet

import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.SnippetsFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SnippetsFragment : BaseDataBindingFragment<SnippetsFragmentViewModel, SnippetsFragmentBinding>() {
    override val fragmentViewModel: SnippetsFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.snippets_fragment

    override fun setupBinding(binding: SnippetsFragmentBinding) {
        super.setupBinding(binding)

        binding.apply {
            snippetList.adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.snippetGroup)
            }
            snippetList.layoutManager = LinearLayoutManager(this@SnippetsFragment.activity)
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.refreshSnippets()
    }
}
