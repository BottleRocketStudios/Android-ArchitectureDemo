package com.bottlerocketstudios.brarchitecture.ui.home

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.HomeFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {
    override val fragmentViewModel: HomeViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()

    override fun getLayoutRes(): Int = R.layout.home_fragment

    override fun setupBinding(binding: HomeFragmentBinding) {
        super.setupBinding(binding)

        fragmentViewModel.userClick.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> findNavController(this@HomeFragment).navigate(R.id.action_homeFragment_to_userFragment)
            }
        })

        binding.repositoryList.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.reposGroup)
                setOnItemClickListener { item, _ ->
                    if (item is ViewModelItem<*> && item.viewModel is RepositoryViewModel) {
                        Toast.makeText(activity, item.viewModel.repository.name as CharSequence, Toast.LENGTH_SHORT).show()
                        activityViewModel.selectRepo(item.viewModel.repository)
                        findNavController(this@HomeFragment).navigate(R.id.action_homeFragment_to_repositoryFragment)
                    }
                }
            }
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        }
    }
}
