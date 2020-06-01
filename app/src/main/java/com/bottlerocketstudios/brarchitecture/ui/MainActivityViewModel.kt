package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.domain.model.Repository

class MainActivityViewModel(app: Application) : BaseViewModel(app) {
    val selectedRepo = MutableLiveData<Repository>()
}
