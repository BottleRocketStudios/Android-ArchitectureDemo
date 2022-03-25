package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SnippetsFragment : BaseFragment<SnippetsFragmentViewModel>() {
    override val fragmentViewModel: SnippetsFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            SnippetsBrowserScreen(state = fragmentViewModel.toState())
        }

    override fun onResume() {
        super.onResume()
        fragmentViewModel.refreshSnippets()
    }
}
