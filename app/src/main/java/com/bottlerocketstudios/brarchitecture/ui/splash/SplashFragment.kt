package com.bottlerocketstudios.brarchitecture.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.compose.splash.SplashScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashViewModel>() {
    override val fragmentViewModel: SplashViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            SplashScreen()
        }
}
