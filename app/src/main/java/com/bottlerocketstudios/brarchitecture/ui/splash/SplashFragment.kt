package com.bottlerocketstudios.brarchitecture.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.SplashFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.LocalDateTime

class SplashFragment : BaseFragment() {
    private val splashViewModel: SplashFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        Timber.v("[onCreateView] demoing desugared java 8 LocalDateTime apis - now=${LocalDateTime.now()}") // TODO: Remove once actually making use of java 8 date time apis elsewhere
        return DataBindingUtil.inflate<SplashFragmentBinding>(inflater, R.layout.splash_fragment, container, false).apply {
            viewModel = splashViewModel
            lifecycleOwner = this@SplashFragment
            splashViewModel.authenticated.observe(viewLifecycleOwner, Observer { authenticated ->
                if (authenticated) {
                    findNavController(this@SplashFragment).navigate(R.id.action_splashFragment_to_homeFragment)
                } else {
                    findNavController(this@SplashFragment).navigate(R.id.action_splashFragment_to_loginFragment)
                }
            })
        }.root
    }
}
