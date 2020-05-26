package com.bottlerocketstudios.brarchitecture.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment

class LoginFragment : BaseFragment() {
    private val loginViewModel: LoginViewModel by lazy {
        getProvidedViewModel(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<LoginFragmentBinding>(inflater, R.layout.login_fragment, container, false).apply {
            viewModel = loginViewModel
            setLifecycleOwner(this@LoginFragment)
            loginViewModel.authenticated.observe(viewLifecycleOwner, Observer { auth: Boolean ->
                Toast.makeText(activity, "LOGGED ${if (auth)"IN" else "OUT"}", Toast.LENGTH_LONG).show()
                if (auth) {
                    Navigation.findNavController(root).navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })
        }.root
    }
}
