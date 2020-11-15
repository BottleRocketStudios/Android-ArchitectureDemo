package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.CreateSnippetFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateSnippetFragment : BaseFragment() {
    private val fragmentViewModel: CreateSnippetFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<CreateSnippetFragmentBinding>(inflater, R.layout.create_snippet_fragment, container, false).apply {
            viewModel = fragmentViewModel
            setLifecycleOwner(this@CreateSnippetFragment)
            fragmentViewModel.done.observe(this@CreateSnippetFragment, Observer {
                findNavController().navigateUp()
            })
        }.root
    }
}
