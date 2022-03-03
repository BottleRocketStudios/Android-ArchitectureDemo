package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_KEY
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthCodeViewModel (
    private val repo: BitbucketRepository,
    buildConfigProvider: BuildConfigProvider,
    private val toaster: Toaster,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    //  Look at including this in BaseViewModel
    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(dispatcherProvider.IO, block = block)

    // UI
    val requestUrl = MutableStateFlow("")
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    ///////////////////////////////////////////////////////////////////////////
    // Callbacks
    ///////////////////////////////////////////////////////////////////////////
    fun onLoginClicked() {
        requestUrl.value = "https://bitbucket.org/site/oauth2/authorize?client_id=${BITBUCKET_KEY}&response_type=code"
    }

    fun onDevOptionsClicked() {
        navigationEvent.postValue(NavigationEvent.Action(R.id.action_authCodeFragment_to_devOptionsFragment))
    }

    fun onSignUpClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://id.atlassian.com/signup?application=bitbucket".toUri())))
    }

    fun onAuthCode(authCode: String) {
        requestUrl.value = ""

        launchIO {
            if (repo.authenticate(authCode)) {
                navigationEvent.postValue(NavigationEvent.Action(R.id.action_authCodeFragment_to_homeFragment))
            } else {
                withContext(dispatcherProvider.Main) {
                    toaster.toast(R.string.login_error)
                }
            }
        }
    }
}
