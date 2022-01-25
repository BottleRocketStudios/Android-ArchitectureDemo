package com.bottlerocketstudios.brarchitecture.ui.auth

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.LoginFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseDataBindingFragment<LoginViewModel, LoginFragmentBinding>() {
    override val fragmentViewModel: LoginViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.login_fragment
}
