package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.model.Repository

class MainActivityViewModel(app: Application) : BaseViewModel(app) {
    val selectedRepo = MutableLiveData<Repository>()
}
