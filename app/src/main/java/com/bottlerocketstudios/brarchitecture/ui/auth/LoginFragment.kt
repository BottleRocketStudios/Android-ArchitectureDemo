package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<LoginFragmentBinding>(inflater, R.layout.login_fragment, container, false).apply {
            viewModel = loginViewModel
            lifecycleOwner = this@LoginFragment
            loginViewModel.authenticated.observe(viewLifecycleOwner, Observer { authenticated: Boolean ->
                when {
                    // TODO: Improve error messaging (update text inputs, show dialog or snackbar, etc)
                    !authenticated -> Toast.makeText(activity, R.string.login_error, Toast.LENGTH_SHORT).show()
                    else -> Navigation.findNavController(root).navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })
            loginViewModel.signupClicked.observe(viewLifecycleOwner, Observer {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://id.atlassian.com/signup?application=bitbucket")))
            })
            loginViewModel.forgotClicked.observe(viewLifecycleOwner, Observer {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://id.atlassian.com/login/resetpassword?application=bitbucket")))
            })
            loginViewModel.devOptionsClicked.observe(viewLifecycleOwner, Observer {
                findNavController().navigate(R.id.action_loginFragment_to_devOptionsFragment)
            })
        }.root
    }
}
