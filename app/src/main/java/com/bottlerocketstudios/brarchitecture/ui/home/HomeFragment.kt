package com.bottlerocketstudios.brarchitecture.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
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
                    if (item is ViewModelItem<*> && item.viewModel is RepositoryViewModel) {
                        Toast.makeText(this@HomeActivity, item.viewModel.repository.name, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            repositoryList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@HomeActivity)
            setLifecycleOwner(this@HomeActivity)
        }
    }
}
 