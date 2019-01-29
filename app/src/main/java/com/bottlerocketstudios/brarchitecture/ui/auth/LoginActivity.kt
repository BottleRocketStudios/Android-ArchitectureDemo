package com.bottlerocketstudios.brarchitecture.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginActivityBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity

class LoginActivity : BaseActivity() {
    private val loginViewModel : LoginViewModel by lazy {
        getProvidedViewModel(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.login_activity).apply {
            viewModel = loginViewModel
            setLifecycleOwner(this@LoginActivity)
            loginViewModel.getAuthenticated().observe(this@LoginActivity, Observer { auth: Boolean ->
                Toast.makeText(this@LoginActivity, "LOGGED IN", Toast.LENGTH_LONG).show()
            })
        }
    }
}
