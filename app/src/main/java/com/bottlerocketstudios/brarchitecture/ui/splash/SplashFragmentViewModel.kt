package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel

class SplashFragmentViewModel(repo: BitbucketRepository) : BaseViewModel() {
    var onAuthenticated: () -> Unit = {}
    var onUnauthenticated: () -> Unit = {}

    init {
        launchIO {
            if (repo.authenticate()) {
                runOnMain {  onAuthenticated() }
            } else {
                runOnMain {  onUnauthenticated() }
            }
        }
    }
}
