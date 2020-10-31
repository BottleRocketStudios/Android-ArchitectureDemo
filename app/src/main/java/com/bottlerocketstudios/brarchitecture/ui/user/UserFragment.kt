package com.bottlerocketstudios.brarchitecture.ui.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.UserFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : BaseFragment() {
    private val fragmentViewModel: UserFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<UserFragmentBinding>(inflater, R.layout.user_fragment, container, false).apply {
            viewModel = fragmentViewModel
            lifecycleOwner = this@UserFragment
            fragmentViewModel.editClicked.observe(viewLifecycleOwner, Observer {
                startActivity( Intent( Intent.ACTION_VIEW,
                    Uri.parse("https://bitbucket.org/account/settings/")))
            })
            fragmentViewModel.logoutClicked.observe(viewLifecycleOwner, Observer {
                findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            })
        }.root
    }
}
