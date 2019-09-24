package com.bottlerocketstudios.brarchitecture.ui

import android.content.ActivityNotFoundException
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.bottlerocketstudios.brarchitecture.BitbucketApplication

abstract class BaseFragment : Fragment() {

    fun <T : ViewModel> getProvidedViewModel(vmClass: Class<T>): T {
        return ViewModelProviders.of(this, (activity?.application as BitbucketApplication).factory).get(vmClass)
    }

    fun <T : ViewModel> getProvidedActivityViewModel(vmClass: Class<T>): T {
        this.activity?.let {
            return ViewModelProviders.of(it, (activity?.application as BitbucketApplication).factory).get(vmClass)
        }
        throw(ActivityNotFoundException())
    }
}