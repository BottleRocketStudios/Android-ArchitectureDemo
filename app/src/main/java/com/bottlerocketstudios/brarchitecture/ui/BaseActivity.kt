package com.bottlerocketstudios.brarchitecture.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.bottlerocketstudios.brarchitecture.BitbucketApplication

abstract class BaseActivity : AppCompatActivity() {

    fun <T : ViewModel> getProvidedViewModel(vmClass: Class<T>): T {
        return ViewModelProviders.of(this, (application as BitbucketApplication).factory).get(vmClass)
    }
}
