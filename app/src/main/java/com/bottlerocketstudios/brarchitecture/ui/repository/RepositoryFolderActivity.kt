package com.bottlerocketstudios.brarchitecture.ui.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFolderActivityBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity

class RepositoryFolderActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, hash: String, path: String): Intent {
            val i = Intent(context, RepositoryFolderActivity::class.java)
            i.putExtra(EXTRA_HASH_ID, hash)
            i.putExtra(EXTRA_PATH_ID, path)
            return i
        }
        
        const val EXTRA_HASH_ID = "hash_id"
        const val EXTRA_PATH_ID = "path_id"
    }
		
    private val repository_folderViewModel: RepositoryFolderActivityViewModel by lazy {
        getProvidedViewModel(RepositoryFolderActivityViewModel::class.java)
    }
		
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<RepositoryFolderActivityBinding>(this, R.layout.repository_folder_activity).apply {
            viewModel = repository_folderViewModel
            setLifecycleOwner(this@RepositoryFolderActivity)
        }
    }
}
