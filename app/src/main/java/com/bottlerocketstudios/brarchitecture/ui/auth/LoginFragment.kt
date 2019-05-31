package com.bottlerocketstudios.brarchitecture.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginActivityBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment

class LoginFragment : BaseFragment() {
    private val loginViewModel : LoginViewModel by lazy {
        getProvidedViewModel(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<LoginActivityBinding>(inflater, R.layout.login_activity, container, false ).apply {
            viewModel = loginViewModel
            setLifecycleOwner(this@LoginFragment)
            loginViewModel.getAuthenticated().observe(this@LoginFragment, Observer { auth: Boolean ->
                Toast.makeText(activity, "LOGGED IN", Toast.LENGTH_LONG).show()
            })
        }.root
    }
}
