package com.bottlerocketstudios.brarchitecture.ui.user

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.UserFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : BaseFragment<UserFragmentViewModel, UserFragmentBinding>() {
    override val fragmentViewModel: UserFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.user_fragment
}
