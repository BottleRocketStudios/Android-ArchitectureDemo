package com.bottlerocketstudios.brarchitecture.ui.home

import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.HomeFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseDataBindingFragment<HomeViewModel, HomeFragmentBinding>() {
    override val fragmentViewModel: HomeViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()

    override fun getLayoutRes(): Int = R.layout.home_fragment

    override fun setupBinding(binding: HomeFragmentBinding) {
        super.setupBinding(binding)

        binding.repositoryList.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(fragmentViewModel.reposGroup)
                setOnItemClickListener { item, _ ->
                    if (item is ViewModelItem<*> && item.viewModel is RepositoryViewModel) {
                        Toast.makeText(activity, item.viewModel.repository.name as CharSequence, Toast.LENGTH_SHORT).show()
                        activityViewModel.selectRepo(item.viewModel.repository)

                        val action = HomeFragmentDirections.actionHomeToRepositoryBrowserFragment(
                            RepositoryBrowserData(
                                repoName = item.viewModel.repository.name ?: ""
                            )
                        )
                        findNavController(this@HomeFragment).navigate(action)
                    }
                }
            }
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        }
    }
}
