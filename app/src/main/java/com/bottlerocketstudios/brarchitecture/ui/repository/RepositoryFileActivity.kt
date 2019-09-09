package com.bottlerocketstudios.brarchitecture.ui.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFileActivityBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFileActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, hash: String, path: String): Intent {
            val i = Intent(context, RepositoryFileActivity::class.java)
            i.putExtra(EXTRA_HASH_ID, hash)
            i.putExtra(EXTRA_PATH_ID, path)
            return i
        }
        
        const val EXTRA_HASH_ID = "hash_id"
        const val EXTRA_PATH_ID = "path_id"
    }
    
    private val repository_fileViewModel: RepositoryFileActivityViewModel by viewModel()
		
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<RepositoryFileActivityBinding>(this, R.layout.repository_file_activity).apply {
            viewModel = repository_fileViewModel
            setLifecycleOwner(this@RepositoryFileActivity)
        }
    }
}
