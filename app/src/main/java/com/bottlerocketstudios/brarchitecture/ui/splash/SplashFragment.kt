package com.bottlerocketstudios.brarchitecture.ui.splash

import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.SplashFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashFragmentViewModel, SplashFragmentBinding>() {
    override val fragmentViewModel: SplashFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.splash_fragment

    override fun setupBinding(binding: SplashFragmentBinding) {
        super.setupBinding(binding)
        fragmentViewModel.authenticated.observe(viewLifecycleOwner, Observer { authenticated ->
            if (authenticated) {
                findNavController(this@SplashFragment).navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController(this@SplashFragment).navigate(R.id.action_splashFragment_to_loginFragment)
            }
        })
    }
}
