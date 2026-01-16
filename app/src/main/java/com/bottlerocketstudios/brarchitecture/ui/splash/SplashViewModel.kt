package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.CognitoRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.inject

class SplashViewModel : BaseViewModel() {
    // DI
    val repo: BitbucketRepository by inject()
    val cognitoRepo: CognitoRepository by inject()

    // Events
    val authEvent: SharedFlow<Unit> = MutableSharedFlow()
    val unAuthEvent: SharedFlow<Unit> = MutableSharedFlow()

    init {
        launchIO {
            if (repo.authenticate() || cognitoRepo.isAuthenticated()) {
                authEvent.emit(Unit)
            } else {
                unAuthEvent.emit(Unit)
            }
        }
    }
}
