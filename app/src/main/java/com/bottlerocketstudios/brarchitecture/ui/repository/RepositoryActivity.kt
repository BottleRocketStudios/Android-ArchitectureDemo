package com.bottlerocketstudios.brarchitecture.ui.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import com.bottlerocketstudios.brarchitecture.ui.user.UserActivity


class RepositoryActivity : BaseActivity() {
    companion object {
        fun newIntent(c: Context, id: String): Intent {
            val i = Intent(c, UserActivity::class.java)
            i.putExtra(EXTRA_REPO_ID, id)
            return i
        }

        const val EXTRA_REPO_ID = "repo_id"
    }

    private val repoViewModel: RepositoryActivityViewModel by lazy {
        getProvidedViewModel(RepositoryActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<com.bottlerocketstudios.brarchitecture.databinding.RepositoryActivityBinding>(this, R.layout.repository_activity).apply {
            viewModel = repoViewModel
            repoViewModel.selectRepository(intent.getStringExtra(EXTRA_REPO_ID))
            setLifecycleOwner(this@RepositoryActivity)
        }
    }
}
 
