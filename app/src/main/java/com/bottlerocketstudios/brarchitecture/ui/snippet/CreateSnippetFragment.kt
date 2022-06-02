package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.compose.snippets.CreateSnippetScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateSnippetFragment : BaseFragment<CreateSnippetFragmentViewModel>() {
    override val fragmentViewModel: CreateSnippetFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            CreateSnippetScreen(state = fragmentViewModel.toState())
        }
}
