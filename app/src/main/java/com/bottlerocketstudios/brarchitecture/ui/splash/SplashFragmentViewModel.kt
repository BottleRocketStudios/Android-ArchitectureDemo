package com.bottlerocketstudios.brarchitecture.ui.splash

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch

class SplashFragmentViewModel(repo: BitbucketRepository, dispatcherProvider: DispatcherProvider) : BaseViewModel() {
    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            if (repo.authenticate()) {
                navigationEvent.postValue(NavigationEvent.Action(R.id.action_splashFragment_to_homeFragment))
            } else {
                navigationEvent.postValue(NavigationEvent.Action(R.id.action_splashFragment_to_loginFragment))
            }
        }
    }
}
