package com.bottlerocketstudios.brarchitecture.ui.splash

import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.inject

class SplashViewModel : BaseViewModel() {
    // region DI
    val repo: BitbucketRepository by inject()
    // endregion

    // region Events
    val authEvent: SharedFlow<Unit> = event()
    val unAuthEvent: SharedFlow<Unit> = event()
    // endregion

    // region Init
    init {
        launchIO {
            val authenticated = showLoadingIndicator.wrapIndicator {
                repo.authenticate()
            }
            if (authenticated) {
                authEvent.tryEmit(Unit)
            } else {
                unAuthEvent.tryEmit(Unit)
            }
        }
    }
    // endregion
}
