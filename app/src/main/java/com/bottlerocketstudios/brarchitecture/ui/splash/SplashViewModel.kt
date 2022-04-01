package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SplashViewModel(repo: BitbucketRepository) : BaseViewModel() {
    val authEvent: SharedFlow<Unit> = MutableSharedFlow()
    val unAuthEvent: SharedFlow<Unit> = MutableSharedFlow()

    init {
        launchIO {
            if (repo.authenticate()) {
                authEvent.emit(Unit)
            } else {
                unAuthEvent.emit(Unit)
            }
        }
    }
}
