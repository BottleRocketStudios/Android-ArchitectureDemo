package com.bottlerocketstudios.brarchitecture.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserActivity : BaseActivity() {
    companion object {
        fun newIntent(c: Context): Intent {
            val i = Intent(c, UserActivity::class.java)
            return i
        }
    }

    private val userViewModel: UserActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<com.bottlerocketstudios.brarchitecture.databinding.UserActivityBinding>(this, R.layout.user_activity).apply {
            viewModel = userViewModel
            setLifecycleOwner(this@UserActivity)
        }
    }
}
 
