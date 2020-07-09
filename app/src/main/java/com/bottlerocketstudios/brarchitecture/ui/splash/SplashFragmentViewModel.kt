package com.bottlerocketstudios.brarchitecture.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch

class SplashFragmentViewModel(app: Application, repo: BitbucketRepository, dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    private val _authenticated = MutableLiveData<Boolean>()
    val authenticated: LiveData<Boolean> = _authenticated

    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            _authenticated.postValue(repo.authenticate())
        }
    }
}
