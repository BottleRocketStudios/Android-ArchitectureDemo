package com.bottlerocketstudios.brarchitecture.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import kotlinx.coroutines.launch

class SplashFragmentViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    private val _authenticated = MutableLiveData<Boolean>()
    val authenticated: LiveData<Boolean> = _authenticated

    init {
        launch {
            _authenticated.postValue(repo.authenticate())
        }
    }
}
