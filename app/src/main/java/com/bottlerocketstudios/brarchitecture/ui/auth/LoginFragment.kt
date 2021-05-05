package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel, LoginFragmentBinding>() {
    override val fragmentViewModel: LoginViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.login_fragment

    override fun setupBinding(binding: LoginFragmentBinding) {
        super.setupBinding(binding)

        fragmentViewModel.authenticated.observe(viewLifecycleOwner, Observer { authenticated: Boolean ->
            when {
                // TODO: Improve error messaging (update text inputs, show dialog or snackbar, etc)
                !authenticated -> Toast.makeText(activity, R.string.login_error, Toast.LENGTH_SHORT).show()
                else -> Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })
        fragmentViewModel.signupClicked.observe(viewLifecycleOwner, Observer {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://id.atlassian.com/signup?application=bitbucket")))
        })
        fragmentViewModel.forgotClicked.observe(viewLifecycleOwner, Observer {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://id.atlassian.com/login/resetpassword?application=bitbucket")))
        })
        fragmentViewModel.devOptionsClicked.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_loginFragment_to_devOptionsFragment)
        })
    }
}
