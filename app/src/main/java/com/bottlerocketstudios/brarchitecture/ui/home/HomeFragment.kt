package com.bottlerocketstudios.brarchitecture.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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

class HomeFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<HomeFragmentBinding>(inflater, R.layout.home_fragment, container, false).apply {
            viewModel = homeViewModel
            homeViewModel.userClick.observe(viewLifecycleOwner, Observer {
                when (it) {
                    true -> findNavController(this@HomeFragment).navigate(R.id.action_homeFragment_to_userFragment)
                }
            })
            repositoryList.apply {
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    add(homeViewModel.reposGroup)
                    setOnItemClickListener { item, _ ->
                        if (item is ViewModelItem<*> && item.viewModel is RepositoryViewModel) {
                            Toast.makeText(activity, item.viewModel.repository.name, Toast.LENGTH_SHORT).show()
                            activityViewModel.selectedRepo.postValue(item.viewModel.repository)
                            findNavController(this@HomeFragment).navigate(R.id.action_homeFragment_to_repositoryFragment)
                        }
                    }
                }
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            }
            lifecycleOwner = this@HomeFragment
        }.root
    }
}
