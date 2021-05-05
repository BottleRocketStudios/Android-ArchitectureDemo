package com.bottlerocketstudios.brarchitecture.ui.user

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.UserFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : BaseFragment<UserFragmentViewModel, UserFragmentBinding>() {
    override val fragmentViewModel: UserFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.user_fragment

    override fun setupBinding(binding: UserFragmentBinding) {
        super.setupBinding(binding)
        fragmentViewModel.editClicked.observe(viewLifecycleOwner, Observer {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://bitbucket.org/account/settings/")))
        })
        fragmentViewModel.logoutClicked.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_userFragment_to_loginFragment)
        })
    }
}
