package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.Routes

class SplashFragmentViewModel(repo: BitbucketRepository) : BaseViewModel() {

    init {
        launchIO {
            if (repo.authenticate()) {
                navigationEvent.postValue(NavigationEvent.Path(Routes.Home))
            } else {
                navigationEvent.postValue(NavigationEvent.Path(Routes.AuthCode))
            }
        }
    }
}
