package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.SplashFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashFragmentViewModel, SplashFragmentBinding>() {
    override val fragmentViewModel: SplashFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.splash_fragment
}
