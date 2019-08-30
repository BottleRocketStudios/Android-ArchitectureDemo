package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginActivityBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import com.bottlerocketstudios.brarchitecture.ui.home.HomeActivity

class LoginActivity : BaseActivity() {
    companion object {
        fun newIntent(c: Context): Intent {
            val i = Intent(c, LoginActivity::class.java)
            return i
        }
    }
    
    private val loginViewModel : LoginViewModel by lazy {
        getProvidedViewModel(LoginViewModel::class.java)
    }   
      
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.login_activity).apply {
            viewModel = loginViewModel
            setLifecycleOwner(this@LoginActivity)
            loginViewModel.authenticated.observe(this@LoginActivity, Observer { auth: Boolean ->
                Toast.makeText(this@LoginActivity, "${auth}", Toast.LENGTH_SHORT).show()
                if (auth) {
                    startActivity(HomeActivity.newIntent(this@LoginActivity))
                }
            })
        }
    }
}
