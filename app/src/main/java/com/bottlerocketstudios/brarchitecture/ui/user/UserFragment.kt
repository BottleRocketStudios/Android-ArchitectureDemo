package com.bottlerocketstudios.brarchitecture.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.UserFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment

class UserFragment : BaseFragment() {
    private val fragmentViewModel: UserFragmentViewModel by lazy {
        getProvidedViewModel(UserFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<UserFragmentBinding>(inflater, R.layout.user_fragment, container, false).apply {
            viewModel = fragmentViewModel
            setLifecycleOwner(this@UserFragment)
        }.root
    }
}
