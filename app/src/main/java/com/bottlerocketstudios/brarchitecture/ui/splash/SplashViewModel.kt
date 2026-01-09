package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.inject

class SplashViewModel : BaseViewModel() {
    // region DI
    val repo: BitbucketRepository by inject()
    // endregion

    // region Events
    val authEvent: SharedFlow<Unit> = MutableSharedFlow()
    val unAuthEvent: SharedFlow<Unit> = MutableSharedFlow()
    // endregion

    // region Init
    init {
        launchIO {
            if (repo.authenticate()) {
                authEvent.emit(Unit)
            } else {
                unAuthEvent.emit(Unit)
            }
        }
    }
    // endregion
}
