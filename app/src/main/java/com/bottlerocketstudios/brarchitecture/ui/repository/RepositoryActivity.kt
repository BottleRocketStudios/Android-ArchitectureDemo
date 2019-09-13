package com.bottlerocketstudios.brarchitecture.ui.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.repository_activity.*


class RepositoryActivity : BaseActivity() {
    companion object {
        fun newIntent(c: Context, repo: Repository): Intent {
            val i = Intent(c, RepositoryActivity::class.java)
            i.putExtra(EXTRA_REPO, repo)
            return i
        }

        const val EXTRA_REPO = "repo"
    }

    private val repoViewModel: RepositoryActivityViewModel by lazy {
        getProvidedViewModel(RepositoryActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<com.bottlerocketstudios.brarchitecture.databinding.RepositoryActivityBinding>(this, R.layout.repository_activity).apply {
            viewModel = repoViewModel
            val repo : Repository = intent.getParcelableExtra(EXTRA_REPO)
            file_list.adapter = GroupAdapter<ViewHolder>().apply {
                add(repoViewModel.filesGroup)
                setOnItemClickListener { item, view ->
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type=="commit_directory") {
                        startActivity(RepositoryFolderActivity.newIntent(this@RepositoryActivity, repo,
                           item.viewModel.file))
                    }
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type=="commit_file") {
                        startActivity(RepositoryFileActivity.newIntent(this@RepositoryActivity, repo, item.viewModel.file))
                    }
                }
            }
            file_list.layoutManager = LinearLayoutManager(this@RepositoryActivity)
            repoViewModel.selectRepository(repo.name)
            setLifecycleOwner(this@RepositoryActivity)
        }
    }
}
 
