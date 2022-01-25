package com.bottlerocketstudios.brarchitecture.ui.snippet

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.CreateSnippetFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseDataBindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateSnippetFragment : BaseDataBindingFragment<CreateSnippetFragmentViewModel, CreateSnippetFragmentBinding>() {
    override val fragmentViewModel: CreateSnippetFragmentViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.create_snippet_fragment
}
