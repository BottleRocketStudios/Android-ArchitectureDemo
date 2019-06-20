package com.bottlerocketstudios.brarchitecture.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.HomeActivityBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class HomeFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by lazy {
        getProvidedViewModel(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<HomeActivityBinding>(inflater, R.layout.home_activity, container, false).apply {
            viewModel = homeViewModel
            repositoryList.apply {
                adapter = GroupAdapter<ViewHolder>().apply {
                    add(homeViewModel.reposGroup)
                    setOnItemClickListener { item, _ ->
                        if (item is ViewModelItem<*> && item.viewModel is RepositoryViewModel) {
                            Toast.makeText(activity, item.viewModel.repository.name, Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(root).navigate(R.id.action_homeFragment_to_repositoryFragment)
                        }
                    }
                }
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            }
            setLifecycleOwner(this@HomeFragment)
        }.root
    }
}
