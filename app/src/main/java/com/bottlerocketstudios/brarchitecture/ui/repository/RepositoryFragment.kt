package com.bottlerocketstudios.brarchitecture.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.RepositoryFragmentBinding
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel

class RepositoryFragment : BaseFragment() {
    private val fragmentViewModel: RepositoryFragmentViewModel by lazy {
        getProvidedViewModel(RepositoryFragmentViewModel::class.java)
    }

    private val activityViewModel: MainActivityViewModel by lazy {
        getProvidedViewModel(MainActivityViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<RepositoryFragmentBinding>(inflater, R.layout.repository_fragment, container, false).apply {
            viewModel = fragmentViewModel
            mainActivityViewModel = activityViewModel
            setLifecycleOwner(this@RepositoryFragment)
        }.root
    }
}
