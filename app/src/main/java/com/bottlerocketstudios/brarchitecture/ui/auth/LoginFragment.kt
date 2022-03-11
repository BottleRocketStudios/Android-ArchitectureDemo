package com.bottlerocketstudios.brarchitecture.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.compose.auth.LoginScreen
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel>() {
    override val fragmentViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                OuterScreenContent(viewModel = fragmentViewModel)
            }
        }
    }

    @Composable
    private fun OuterScreenContent(viewModel: LoginViewModel) {
        ArchitectureDemoTheme {
            val state = viewModel.toState()
            LoginScreen(state = state)
        }
    }
}
