package com.bottlerocketstudios.brarchitecture.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.compose.auth.AuthCodeScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthCodeFragment : BaseFragment<AuthCodeViewModel>() {
    override val fragmentViewModel: AuthCodeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            AuthCodeScreen(state = fragmentViewModel.toState())
        }
}
