package com.bottlerocketstudios.brarchitecture.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryActivity
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.bottlerocketstudios.brarchitecture.ui.user.UserActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.home_activity.*


class HomeActivity : BaseActivity() {
    companion object {
        fun newIntent(c: Context): Intent {
            val i = Intent(c, HomeActivity::class.java)
            return i
        }
    }

    private val homeViewModel: HomeViewModel by lazy {
        getProvidedViewModel(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<com.bottlerocketstudios.brarchitecture.databinding.HomeActivityBinding>(this, R.layout.home_activity).apply {
            viewModel = homeViewModel
            repository_list.adapter = GroupAdapter<ViewHolder>().apply { 
                add(homeViewModel.reposGroup)
                setOnItemClickListener { item, view -> 
                    if (item is ViewModelItem<*> && item.viewModel is RepositoryViewModel && item.viewModel.repository.name!=null) {
                        startActivity(RepositoryActivity.newIntent(this@HomeActivity, item.viewModel.repository))
                    }
                }
            }
            homeViewModel.userClick.observe(this@HomeActivity, Observer {
                when(it) {
                    true ->
                        startActivity(UserActivity.newIntent(this@HomeActivity))
                }
            })
            repositoryList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@HomeActivity)
            setLifecycleOwner(this@HomeActivity)
        }
    }
}
 