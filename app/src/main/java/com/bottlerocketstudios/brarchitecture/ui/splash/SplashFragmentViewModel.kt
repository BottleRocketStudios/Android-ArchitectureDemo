package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel

class SplashFragmentViewModel(repo: BitbucketRepository) : BaseViewModel() {
    init {
        launchIO {
            if (repo.authenticate()) {
                navigationEvent.postValue(NavigationEvent.Action(R.id.action_splashFragment_to_homeFragment))
            } else {
                navigationEvent.postValue(NavigationEvent.Action(R.id.action_splashFragment_to_authCodeFragment))
            }
        }
    }
}
