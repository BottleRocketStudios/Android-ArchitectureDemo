package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Intent
import androidx.core.net.toUri
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_KEY
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.inject

class AuthCodeViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()
    private val buildConfigProvider: BuildConfigProvider by inject()

    // UI
    val requestUrl = MutableStateFlow("")
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    // Events
    val devOptionsEvent: SharedFlow<Unit> = MutableSharedFlow()
    val homeEvent: SharedFlow<Unit> = MutableSharedFlow()

    // /////////////////////////////////////////////////////////////////////////
    // Callbacks
    // /////////////////////////////////////////////////////////////////////////
    fun onLoginClicked() {
        requestUrl.value = "https://bitbucket.org/site/oauth2/authorize?client_id=$BITBUCKET_KEY&response_type=code"
    }

    fun onDevOptionsClicked() {
        launchIO { devOptionsEvent.emit(Unit) }
    }

    fun onSignUpClicked() =
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://id.atlassian.com/signup?application=bitbucket".toUri())))

    fun onAuthCode(authCode: String) {
        requestUrl.value = ""

        launchIO {
            if (repo.authenticate(authCode)) {
                homeEvent.emit(Unit)
            } else {
                handleError(R.string.login_error)
            }
        }
    }
}
