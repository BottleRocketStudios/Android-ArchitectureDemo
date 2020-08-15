package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository

class MainActivityViewModel(app: Application, val repo: BitbucketRepository) : BaseViewModel(app) {
    val selectedRepo = MutableLiveData<Repository>()
    val showToolbar = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
}
